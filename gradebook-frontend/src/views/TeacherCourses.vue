<template>
  <div class="ma-5">
    <v-row>
      <v-card class="ma-3 pa-3">
        <v-card-title>Add course</v-card-title>
        <v-card-text>
          <v-text-field
            v-model="courseName"
            label="Course name"
            prepend-icon="mdi-account-group"
          ></v-text-field>
          <v-select
            v-model="subjectName"
            :items="subjectNames"
            label="Subject"
          ></v-select>
          <v-card-actions>
            <v-spacer />
            <v-btn @click="addCourse" :loading="courseSpinner" right>
              Add course
            </v-btn>
          </v-card-actions>
        </v-card-text>
      </v-card>

      <v-card class="ma-3 pa-3">
        <v-card-title>Courses</v-card-title>
        <v-list>
          <v-list-item-group>
            <v-list-item v-for="course in courses" :key="course.name">
              <v-list-item-icon>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on }">
                    <v-btn
                      icon
                      v-on="on"
                      @click="deleteCourse(course)"
                      :loading="courseDeleteSpinners[course.name]"
                    >
                      <v-icon>mdi-delete</v-icon>
                    </v-btn>
                  </template>
                  <span>Remove course</span>
                </v-tooltip>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on }">
                    <v-btn
                      icon
                      v-on="on"
                      @click="goToCourse(course)"
                      :loading="courseDeleteSpinners[course.name]"
                    >
                      <v-icon>mdi-details</v-icon>
                    </v-btn>
                  </template>
                  <span>Course details</span>
                </v-tooltip>
              </v-list-item-icon>
              <v-list-item-content v-text="course.name" />
            </v-list-item>
          </v-list-item-group>
        </v-list>
      </v-card>
    </v-row>
  </div>
</template>

<script>
import { queryCourses, addCourse, deleteCourse } from "../api/course";
import { querySubjects } from "../api/subject";

export default {
  name: "teacher-courses",

  data() {
    return {
      courses: [],
      courseName: "",
      courseSpinner: false,
      courseDeleteSpinners: {},

      subjects: [],
      subjectNames: [],
      subjectName: ""
    };
  },

  created: function() {
    this.queryCourses();
    this.querySubjects();
  },

  methods: {
    error(message) {
      this.snackbarText = message;
      this.snackbarColor = "error";
      this.showSnackbar = true;
    },

    async querySubjects() {
      this.subjects = await querySubjects();
      this.subjectNames = this.subjects.map(e => e.name);
      console.log(this.subjectNames);
    },

    async queryCourses() {
      this.courses = await queryCourses();

      for (const g of this.courses) {
        this.courseDeleteSpinners[g.name] = false;
      }
    },

    async addCourse() {
      this.courseSpinner = true;

      let subjectId = this.subjects.filter(e => e.name == this.subjectName)[0]
        .id;

      try {
        await addCourse(this.courseName, subjectId);

        this.courseName = "";
        this.queryCourses();
      } catch {
        this.error("Unable to add a course");
      }

      this.courseSpinner = false;
    },

    async deleteCourse(course) {
      this.courseDeleteSpinners[course.name] = true;

      try {
        await deleteCourse(course.id);
        this.queryCourses();
      } catch {
        this.error("Unable to remove a course");
      }

      this.courseDeleteSpinners[course.name] = false;
    },

    async goToCourse(course) {
      this.$router.push(`/teacher-courses/${course.id}`);
    }
  }
};
</script>
