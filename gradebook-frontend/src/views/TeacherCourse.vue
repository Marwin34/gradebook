<template>
  <div>
    <AddGroupToCourseDialog
      v-bind:courseId="$route.params.courseId"
      v-bind:visible="addGroupToCourseDialogVisible"
      v-on:close="addGroupDialogClosed"
    ></AddGroupToCourseDialog>

    <AddAssignmentToCourseDialog
      v-bind:courseId="$route.params.courseId"
      v-bind:visible="addAssignmentToCourseDialogVisible"
      v-on:close="addAssignmentDialogClosed"
    ></AddAssignmentToCourseDialog>

    <ManageAssignmentDialog
      v-bind:courseId="$route.params.courseId"
      v-bind:assignmentId="selectedAssignmentId"
      v-bind:visible="manageAssignmentDialogVisible"
      v-on:close="manageAssignmentDialogClosed"
    ></ManageAssignmentDialog>

    <ManageGradeDialog
      v-bind:courseId="$route.params.courseId"
      v-bind:assignmentId="selectedAssignmentId"
      v-bind:studentId="selectedStudentId"
      v-bind:gradeId="selectedGradeId"
      v-bind:visible="manageGradeDialogVisible"
      v-on:close="manageGradeDialogClosed"
    ></ManageGradeDialog>

    <v-row justify="center">
      <v-col cols="12" md="12">
        <v-card class="pa-3">
          <v-row no-gutters>
            <v-btn @click="manageGroups" min-width="15em" class="mr-4">
              Manage groups
            </v-btn>
            <v-btn @click="addAssignment" width="15em">
              Add assignment
            </v-btn>
          </v-row>
          <v-row>
            <v-col cols="12" md="12">
              <v-data-table
                :headers="table.headers"
                :items="table.items"
                md="12"
                hide-default-footer
                hide-default-header
                class="elevation-1"
              >
                <template v-slot:header="props">
                  <thead>
                    <th>Student</th>
                    <th
                      v-for="(header, index) in props.props.headers"
                      :key="index"
                    >
                      <v-btn
                        text
                        width="100%"
                        @click="manageAssignment(header.assignmentId)"
                      >
                        {{ header.assignmentName }}
                      </v-btn>
                    </th>
                  </thead>
                </template>
                <template v-slot:item="props">
                  <tr>
                    <td>{{ props.item.studentName }}</td>
                    <td
                      v-for="(grade, index) in props.item.grades"
                      :key="index"
                    >
                      <Grade
                        v-bind:grade="grade"
                        v-bind:courseId="$route.params.courseId"
                        v-bind:assignmentId="props.headers[index].assignmentId"
                        v-bind:studentId="props.item.studentId"
                        v-on:click="gradeClicked"
                      />
                    </td>
                  </tr>
                </template>
              </v-data-table>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import AddGroupToCourseDialog from "../components/AddGroupToCourseDialog";
import AddAssignmentToCourseDialog from "../components/AddAssignmentToCourseDialog";
import ManageAssignmentDialog from "../components/ManageAssignmentDialog";
import ManageGradeDialog from "../components/ManageGradeDialog";
import Grade from "../components/Grade";

import { queryCourseTable } from "../api/course";
export default {
  name: "teacher-course",
  components: {
    AddGroupToCourseDialog,
    AddAssignmentToCourseDialog,
    ManageAssignmentDialog,
    ManageGradeDialog,
    Grade
  },
  data() {
    return {
      addGroupToCourseDialogVisible: false,
      addAssignmentToCourseDialogVisible: false,

      manageAssignmentDialogVisible: false,
      manageGradeDialogVisible: false,

      selectedAssignmentId: 0,
      selectedStudentId: 0,
      selectedGradeId: 0,

      table: {}
    };
  },

  created: function() {
    this.queryCourseTable();
  },

  methods: {
    async queryCourseTable() {
      let rawTable = await queryCourseTable(this.$route.params.courseId);

      let table = {};
      table.headers = rawTable.headers;
      table.items = rawTable.rows;
      this.table = table;
    },

    async manageGroups() {
      this.addGroupToCourseDialogVisible = true;
    },

    async addGroupDialogClosed() {
      this.addGroupToCourseDialogVisible = false;
      this.queryCourseTable();
    },

    async addAssignment() {
      this.addAssignmentToCourseDialogVisible = true;
    },

    async addAssignmentDialogClosed() {
      this.addAssignmentToCourseDialogVisible = false;
      this.queryCourseTable();
    },

    async manageAssignment(assignmentId) {
      this.selectedAssignmentId = assignmentId;
      this.manageAssignmentDialogVisible = true;
    },

    async manageAssignmentDialogClosed() {
      this.manageAssignmentDialogVisible = false;
      this.queryCourseTable();
    },

    async gradeClicked(courseId, assignmentId, studentId, gradeId) {
      this.selectedAssignmentId = assignmentId;
      this.selectedStudentId = studentId;
      this.selectedGradeId = gradeId;
      this.manageGradeDialogVisible = true;
    },

    async manageGradeDialogClosed() {
      this.manageGradeDialogVisible = false;
      this.queryCourseTable();
    }
  }
};
</script>
