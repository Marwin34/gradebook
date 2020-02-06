<template>
  <div>
    <v-dialog v-model="dialog" width="500" @input="v => v || $emit('close')">
      <v-card>
        <v-card-title class="headline grey lighten-2" primary-title>
          Manage grade
        </v-card-title>
        <v-btn
          v-for="(grade, index) in grades"
          :key="index"
          color="primary"
          text
          @click="onGradeChoosen(grade)"
          :loading="buttonLoading"
        >
          {{ grade }}
        </v-btn>
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="$emit('close')">
            Close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { removeGrade, addGrade } from "../api/grade";

export default {
  name: "ManageGradeDialog",

  props: ["visible", "courseId", "assignmentId", "studentId", "gradeId"],

  data() {
    return {
      dialog: false,
      buttonLoading: false,
      grades: ["1", "2", "3", "4", "5", "6"].flatMap(e => ["-" + e, e, "+" + e])
    };
  },
  methods: {
    async onGradeChoosen(grade) {
      this.buttonLoading = true;

      if (this.gradeId != -1) {
        await removeGrade(this.courseId, this.assignmentId, this.gradeId);
      }
      await addGrade(this.courseId, this.assignmentId, this.studentId, grade);

      this.$emit("close");
    },

    async onDialogShown() {
      this.buttonLoading = false;
    }
  },

  watch: {
    visible: {
      immediate: true,
      handler(val) {
        this.dialog = val;
        if (val) {
          this.onDialogShown();
        }
      }
    }
  }
};
</script>
