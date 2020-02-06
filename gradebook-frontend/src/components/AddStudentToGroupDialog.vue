<template>
  <div>
    <v-dialog v-model="visible" width="500" @input="v => v || $emit('close')">
      <v-card>
        <v-card-title class="headline grey lighten-2" primary-title>
          {{ group.name }}
        </v-card-title>
        <v-card-text>
          <v-list>
            <v-list-item v-for="student in students" :key="student.email">
              <v-checkbox
                v-model="student.selected"
                :label="`${student.firstName} ${student.lastName}`"
                @change="onCheckboxChange(student)"
              ></v-checkbox>
            </v-list-item>
          </v-list>
        </v-card-text>
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
import { queryStudents } from "../api/student";
import {
  queryGroup,
  addStudentToGroup,
  removeStudentFromGroup
} from "../api/group";

export default {
  name: "addStudentToGroupDialog",

  props: ["visible", "groupId"],

  data() {
    return {
      group: {},
      students: [],
      studentsInGroup: []
    };
  },

  created: function() {
    this.queryStudents();
  },

  methods: {
    async logout() {},

    async queryStudents() {},

    async queryGroup(groupId) {
      let students = await queryStudents();
      this.group = await queryGroup(groupId);

      students.forEach(student => (student.selected = false));
      this.group.students.forEach(groupStudent =>
        students.forEach(student => {
          if (groupStudent.id === student.id) {
            student.selected = true;
          }
        })
      );

      this.students = students;
    },

    async onCheckboxChange(student) {
      if (student.selected) {
        await addStudentToGroup(student.id, this.group.id);
      } else {
        await removeStudentFromGroup(student.id, this.group.id);
      }
    }
  },

  watch: {
    groupId: {
      immediate: true,
      handler(val) {
        this.queryGroup(val);
      }
    }
  }
};
</script>
