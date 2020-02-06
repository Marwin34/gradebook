import axios from "axios";
import store from "../store";

export async function addGrade(courseId, assignmentId, studentId, gradeValue) {
  if (!store.getters.isTeacher) {
    throw "Only teacher is able to add a grade";
  }

  await axios.post(
    `/api/teacher/courses/${courseId}/assignments/${assignmentId}/grades`,
    {
      studentId,
      gradeValue
    }
  );
}

export async function removeGrade(courseId, assignmentId, gradeId) {
  if (!store.getters.isTeacher) {
    throw "Only teacher is able to delete a grade";
  }

  await axios.delete(
    `/api/teacher/courses/${courseId}/assignments/${assignmentId}/grades/${gradeId}`
  );
}
