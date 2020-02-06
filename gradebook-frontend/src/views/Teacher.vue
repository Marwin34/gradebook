<template>
  <div class="ma-5">
    <AddStudentToGroupDialog
      v-bind:groupId="selectedGroupId"
      v-bind:visible="addStudentsToGroupDialog"
      v-on:close="addStudentsToGroupDialog = false"
    ></AddStudentToGroupDialog>
    <v-row>
      <v-card class="ma-3 pa-3">
        <v-card-title>Add student</v-card-title>
        <v-card-text>
          <v-text-field
            v-model="studentEmail"
            label="E-mail"
            type="email"
            prepend-icon="mdi-email"
          />
          <v-text-field
            v-model="studentFirstName"
            label="First name"
            prepend-icon="mdi-account"
          ></v-text-field>
          <v-text-field
            v-model="studentLastName"
            label="Last name"
            prepend-icon="mdi-account-card-details"
          ></v-text-field>
          <v-card-actions>
            <v-spacer />
            <v-btn @click="addStudent" :loading="studentSpinner" right>
              Add student
            </v-btn>
          </v-card-actions>
        </v-card-text>
      </v-card>

      <v-card class="ma-3 pa-3">
        <v-card-title>Students</v-card-title>
        <v-list>
          <v-list-item-group>
            <v-list-item v-for="student in students" :key="student.email">
              <v-list-item-icon>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on }">
                    <v-btn
                      icon
                      v-on="on"
                      @click="deleteStudent(student)"
                      :loading="studentDeleteSpinners[student.email]"
                    >
                      <v-icon>mdi-delete</v-icon>
                    </v-btn>
                  </template>
                  <span>Remove student</span>
                </v-tooltip>
              </v-list-item-icon>

              <v-list-item-content>
                {{ student.firstName }}
                {{ student.lastName }}
                ({{ student.email }})
              </v-list-item-content>
            </v-list-item>
          </v-list-item-group>
        </v-list>
      </v-card>
    </v-row>

    <v-row>
      <v-card class="ma-3 pa-3">
        <v-card-title>Add group</v-card-title>
        <v-card-text>
          <v-text-field
            v-model="groupName"
            label="Group name"
            prepend-icon="mdi-account-group"
          ></v-text-field>
          <v-card-actions>
            <v-spacer />
            <v-btn @click="addGroup" :loading="groupSpinner" right>
              Add group
            </v-btn>
          </v-card-actions>
        </v-card-text>
      </v-card>

      <v-card class="ma-3 pa-3">
        <v-card-title>Groups</v-card-title>
        <v-list>
          <v-list-item-group>
            <v-list-item v-for="group in groups" :key="group.name">
              <v-list-item-icon>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on }">
                    <v-btn
                      icon
                      v-on="on"
                      @click="deleteGroup(group)"
                      :loading="groupDeleteSpinners[group.name]"
                    >
                      <v-icon>mdi-delete</v-icon>
                    </v-btn>
                  </template>
                  <span>Remove group</span>
                </v-tooltip>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on }">
                    <v-btn icon v-on="on" @click="addStudentsToGroup(group)">
                      <v-icon>mdi-account-multiple-plus</v-icon>
                    </v-btn>
                  </template>
                  <span>Add students</span>
                </v-tooltip>
              </v-list-item-icon>
              <v-list-item-content v-text="group.name" />
            </v-list-item>
          </v-list-item-group>
        </v-list>
      </v-card>
    </v-row>
  </div>
</template>

<script>
import { queryStudents, addStudent, deleteStudent } from "../api/student";
import { queryGroups, addGroup, deleteGroup } from "../api/group";
import AddStudentToGroupDialog from "../components/AddStudentToGroupDialog";

export default {
  name: "teacher",
  components: {
    AddStudentToGroupDialog
  },
  data() {
    return {
      addStudentsToGroupDialog: false,

      students: [],
      studentEmail: "",
      studentFirstName: "",
      studentLastName: "",
      studentSpinner: false,
      studentDeleteSpinners: {},

      groups: [],
      groupName: "",
      groupSpinner: false,
      groupDeleteSpinners: {}
    };
  },

  created: function() {
    this.queryStudents();
    this.queryGroups();
  },

  methods: {
    error(message) {
      this.snackbarText = message;
      this.snackbarColor = "error";
      this.showSnackbar = true;
    },

    addStudentsToGroup(group) {
      this.selectedGroupId = group.id;
      this.addStudentsToGroupDialog = true;
    },

    async queryStudents() {
      this.students = await queryStudents();

      for (const s of this.students) {
        this.studentDeleteSpinners[s.email] = false;
      }
    },

    async addStudent() {
      this.studentSpinner = true;

      try {
        await addStudent(
          this.studentEmail,
          this.studentFirstName,
          this.studentLastName
        );

        this.studentEmail = this.studentFirstName = this.studentLastName = "";
        this.queryStudents();
      } catch (error) {
        this.error("Unable to add a new student!");
      }

      this.studentSpinner = false;
    },

    async deleteStudent(student) {
      this.studentDeleteSpinners[student.email] = true;

      try {
        await deleteStudent(student.id);
        this.queryStudents();
      } catch {
        this.error("Unable to remove student");
      }

      this.studentDeleteSpinners[student.email] = false;
    },

    async queryGroups() {
      this.groups = await queryGroups();

      for (const g of this.groups) {
        this.groupDeleteSpinners[g.name] = false;
      }
    },

    async addGroup() {
      this.groupSpinner = true;

      try {
        await addGroup(this.groupName);

        this.groupName = "";
        this.queryGroups();
      } catch {
        this.error("Unable to add a group");
      }

      this.groupSpinner = false;
    },

    async deleteGroup(group) {
      this.groupDeleteSpinners[group.name] = true;

      try {
        await deleteGroup(group.id);
        this.queryGroups();
      } catch {
        this.error("Unable to remove a group");
      }

      this.groupDeleteSpinners[group.name] = false;
    }
  }
};
</script>
