<template>
  <div class="ma-5">
    <v-row align="start">
      <v-card class="ma-3 pa-3">
        <v-card-title>Add teacher</v-card-title>
        <v-card-text>
          <v-text-field
            v-model="teacherEmail"
            label="E-mail"
            type="email"
            prepend-icon="mdi-email"
          />
          <v-text-field
            v-model="teacherFirstName"
            label="First name"
            prepend-icon="mdi-account"
          ></v-text-field>
          <v-text-field
            v-model="teacherLastName"
            label="Last name"
            prepend-icon="mdi-account-card-details"
          ></v-text-field>
          <v-card-actions>
            <v-spacer />
            <v-btn @click="addTeacher" :loading="teacherSpinner" right>
              Add teacher
            </v-btn>
          </v-card-actions>
        </v-card-text>
      </v-card>

      <v-card class="ma-3 pa-3">
        <v-card-title>Teachers</v-card-title>
        <v-list>
          <v-list-item-group>
            <v-list-item v-for="teacher in teachers" :key="teacher.email">
              <v-list-item-icon>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on }">
                    <v-btn
                      icon
                      v-on="on"
                      @click="deleteTeacher(teacher)"
                      :loading="teacherDeleteSpinners[teacher.email]"
                    >
                      <v-icon>mdi-delete</v-icon>
                    </v-btn>
                  </template>
                  <span>Remove teacher</span>
                </v-tooltip>
              </v-list-item-icon>
              <v-list-item-content>
                {{ teacher.firstName }}
                {{ teacher.lastName }}
                ({{ teacher.email }})
              </v-list-item-content>
            </v-list-item>
          </v-list-item-group>
        </v-list>
      </v-card>
    </v-row>

    <v-row align="start">
      <v-card class="ma-3 pa-3">
        <v-card-title>Add subject</v-card-title>
        <v-card-text>
          <v-text-field
            v-model="subjectName"
            label="Subject name"
            prepend-icon="mdi-book-open-page-variant"
          />
          <v-card-actions>
            <v-spacer />
            <v-btn @click="addSubject" :loading="subjectSpinner" right>
              Add subject
            </v-btn>
          </v-card-actions>
        </v-card-text>
      </v-card>

      <v-card class="ma-3 pa-3">
        <v-card-title>Subjects</v-card-title>
        <v-list>
          <v-list-item-group>
            <v-list-item v-for="subject in subjects" :key="subject.name">
              <v-list-item-icon>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on }">
                    <v-btn
                      icon
                      v-on="on"
                      @click="deleteSubject(subject)"
                      :loading="subjectDeleteSpinners[subject.name]"
                    >
                      <v-icon>mdi-delete</v-icon>
                    </v-btn>
                  </template>
                  <span>Remove subject</span>
                </v-tooltip>
              </v-list-item-icon>
              <v-list-item-content v-text="subject.name" />
            </v-list-item>
          </v-list-item-group>
        </v-list>
      </v-card>
    </v-row>
    <v-snackbar
      :color="snackbarColor"
      v-model="showSnackbar"
      :timeout="snackbarDisapears ? 6000 : 0"
      top
    >
      {{ snackbarText }}
      <v-btn color="black" text @click="showSnackbar = false">Close</v-btn>
    </v-snackbar>
  </div>
</template>

<script>
import { queryTeachers, addTeacher, deleteTeacher } from "../api/teacher";
import { querySubjects, addSubject, deleteSubject } from "../api/subject";

export default {
  name: "admin",

  data() {
    return {
      teachers: [],
      teacherEmail: "",
      teacherFirstName: "",
      teacherLastName: "",
      teacherSpinner: false,
      teacherDeleteSpinners: {},

      subjects: [],
      subjectName: "",
      subjectSpinner: false,
      subjectDeleteSpinners: {},

      showSnackbar: false,
      snackbarColor: "",
      snackbarText: "",
      snackbarDisapears: true
    };
  },

  created: function() {
    this.queryTeachers();
    this.querySubjects();
  },

  methods: {
    error(message) {
      this.snackbarText = message;
      this.snackbarColor = "error";
      this.showSnackbar = true;
      this.snackbarDisapears = true;
    },

    success(message) {
      this.snackbarText = message;
      this.snackbarColor = "success";
      this.showSnackbar = true;
      this.snackbarDisapears = true;
    },

    async queryTeachers() {
      this.teachers = await queryTeachers();
    },

    async addTeacher() {
      this.teacherSpinner = true;

      try {
        const password = await addTeacher(
          this.teacherEmail,
          this.teacherFirstName,
          this.teacherLastName
        );

        this.teacherEmail = this.teacherFirstName = this.teacherLastName = "";
        this.queryTeachers();

        this.success(`Password: ${password}`);
        this.snackbarDisapears = false;
      } catch {
        this.error("Unable to add a new teacher!");
      }

      this.teacherSpinner = false;
    },

    async deleteTeacher(teacher) {
      this.teacherDeleteSpinners[teacher.email] = true;

      try {
        await deleteTeacher(teacher.id);
        this.queryTeachers();
      } catch {
        this.error("Unable to remove a teacher!");
      }

      this.teacherDeleteSpinners[teacher.email] = false;
    },

    async querySubjects() {
      this.subjects = await querySubjects();
    },

    async addSubject() {
      this.subjectSpinner = true;

      try {
        await addSubject(this.subjectName);

        this.subjectName = "";
        this.querySubjects();

        this.success(`Subject ${this.subjectName} added successfully`);
      } catch (error) {
        this.error("Unable to add a new subject!");
      }

      this.subjectSpinner = false;
    },

    async deleteSubject(subject) {
      this.subjectDeleteSpinners[subject.name] = true;

      try {
        await deleteSubject(subject.id);
        this.querySubjects();
      } catch {
        this.error("Unable to remove a subject!");
      }

      this.subjectDeleteSpinners[subject.name] = false;
    }
  }
};
</script>
