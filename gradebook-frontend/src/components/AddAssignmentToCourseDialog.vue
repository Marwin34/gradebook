<template>
  <div>
    <v-dialog v-model="visible" width="500" @input="v => v || $emit('close')">
      <v-card>
        <v-card-title class="headline grey lighten-2" primary-title>
          Add new assignment
        </v-card-title>
        <v-card-text>
          <v-text-field
            v-model="newAssignmentName"
            label="Name"
            prepend-icon="mdi-textbox"
          />
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="$emit('close')">
            Close
          </v-btn>
          <v-btn color="primary" text @click="onAddClick">
            Add
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { addAssignment } from "../api/assignment";
export default {
  name: "AddAssignmentToCourseDialog",

  props: ["visible", "courseId"],

  data() {
    return {
      newAssignmentName: ""
    };
  },
  methods: {
    async onAddClick() {
      await addAssignment(this.courseId, this.newAssignmentName);
      this.$emit("close");
    }
  }
};
</script>
