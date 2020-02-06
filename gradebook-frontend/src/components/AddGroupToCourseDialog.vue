<template>
  <div>
    <v-dialog v-model="visible" width="500" @input="v => v || $emit('close')">
      <v-card>
        <v-card-title class="headline grey lighten-2" primary-title>
          Manage groups
        </v-card-title>
        <v-card-text>
          <v-list>
            <v-list-item v-for="group in groups" :key="group.id">
              <v-checkbox
                v-model="group.selected"
                :label="`${group.name}`"
                @change="onCheckboxChange(group)"
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
import { queryGroups } from "../api/group";
import {
  queryGroupsInCourse,
  addGroupToCourse,
  removeGroupFromCourse
} from "../api/course";

export default {
  name: "addStudentToGroupDialog",

  props: ["visible", "courseId"],

  data() {
    return {
      groups: [],
      groupsInCourse: []
    };
  },

  created: function() {
    this.queryGroups();
  },

  methods: {
    async queryGroups(courseId) {
      let groups = await queryGroups();
      this.groupsInCourse = await queryGroupsInCourse(courseId);

      groups.forEach(group => (group.selected = false));
      this.groupsInCourse.forEach(groupInCourse =>
        groups.forEach(group => {
          if (groupInCourse.id === group.id) {
            group.selected = true;
          }
        })
      );

      this.groups = groups;
    },

    async onCheckboxChange(group) {
      if (group.selected) {
        await addGroupToCourse(this.courseId, group.id);
      } else {
        await removeGroupFromCourse(this.courseId, group.id);
      }
    }
  },

  watch: {
    courseId: {
      immediate: true,
      handler(val) {
        this.queryGroups(val);
      }
    }
  }
};
</script>
