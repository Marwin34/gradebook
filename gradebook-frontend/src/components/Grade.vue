<template>
  <span v-if="grade.gradeType === 'NOT_IN_ASSIGNMENT'">
    x
  </span>
  <span v-else-if="grade.gradeType === 'NONE'">
    <v-btn
      text
      width="100%"
      @click="gradeClicked(courseId, assignmentId, studentId, -1)"
    >
      -
    </v-btn>
  </span>
  <span v-else>
    <v-btn
      text
      width="100%"
      @click="gradeClicked(courseId, assignmentId, studentId, grade.gradeId)"
    >
      {{ grade.gradeText }}
    </v-btn>
  </span>
</template>

<script>
export default {
  name: "grade",
  props: ["grade", "courseId", "assignmentId", "studentId"],

  data() {
    return {};
  },

  methods: {
    gradeClicked(courseId, assignmentId, studentId, gradeId) {
      this.$emit("click", courseId, assignmentId, studentId, gradeId);
    }
  }
};
</script>
