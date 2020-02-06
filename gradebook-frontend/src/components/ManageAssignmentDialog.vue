<template>
  <div>
    <v-dialog v-model="dialog" width="500" @input="v => v || $emit('close')">
      <v-card>
        <v-card-title class="headline grey lighten-2" primary-title>
          Manage assignment
        </v-card-title>
        <v-card-text>Assignment: {{ this.assignment.name }}</v-card-text>
        <v-list>
          <v-list-item v-for="group in groupsInCourse" :key="group.id">
            <v-checkbox
              v-model="group.selected"
              :label="`${group.name}`"
            ></v-checkbox>
          </v-list-item>
        </v-list>
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="$emit('close')">
            Close
          </v-btn>
          <v-btn
            color="error"
            text
            @click="onDeleteClick"
            :loading="buttonLoading"
          >
            Delete
          </v-btn>
          <v-btn
            color="primary"
            text
            @click="onOkClick"
            :loading="buttonLoading"
          >
            Ok
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import {
  removeAssignment,
  queryAssignment,
  queryGroupsInAssignment,
  addGroupToAssignment,
  removeGroupFromAssignment
} from "../api/assignment";
import { queryGroupsInCourse } from "../api/course";

export default {
  name: "ManageAssignmentDialog",

  props: ["visible", "courseId", "assignmentId"],

  data() {
    return {
      dialog: false,
      newAssignmentName: "",
      assignment: {},
      groupsInCourse: [],
      groupsInAssignment: [],
      buttonLoading: false
    };
  },
  methods: {
    async onOkClick() {
      this.buttonLoading = true;

      for (const groupInCourse of this.groupsInCourse) {
        if (groupInCourse.selected != groupInCourse.wasSelected) {
          if (groupInCourse.selected) {
            await addGroupToAssignment(
              this.courseId,
              this.assignmentId,
              groupInCourse.id
            );
          } else {
            await removeGroupFromAssignment(
              this.courseId,
              this.assignmentId,
              groupInCourse.id
            );
          }
        }
      }
      this.$emit("close");
    },

    async onDeleteClick() {
      this.buttonLoading = true;

      await removeAssignment(this.courseId, this.assignmentId);
      this.$emit("close");
    },

    async onDialogShown() {
      this.buttonLoading = false;
      this.assignment = await queryAssignment(this.courseId, this.assignmentId);
      let groupsInCourse = await queryGroupsInCourse(this.courseId);
      this.groupsInAssignment = await queryGroupsInAssignment(
        this.courseId,
        this.assignmentId
      );
      this.newAssignmentName = this.assignment.name;

      groupsInCourse.forEach(groupInCourse => (groupInCourse.selected = false));
      groupsInCourse.forEach(
        groupInCourse => (groupInCourse.wasSelected = false)
      );

      this.groupsInAssignment.forEach(groupInAssignment =>
        groupsInCourse.forEach(groupInCourse => {
          if (groupInCourse.id === groupInAssignment.id) {
            groupInCourse.selected = true;
            groupInCourse.wasSelected = true;
          }
        })
      );

      this.groupsInCourse = groupsInCourse;
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
