import axios from "axios";
import store from "../store";

export async function queryCourseTable(courseId) {
  const resp = await axios.get(`/api/teacher/courses/${courseId}/table`);
  return resp.data;
}

export async function queryCourses() {
  const resp = await axios.get("/api/teacher/courses");
  return resp.data;
}

export async function addCourse(name, subjectId) {
  if (!store.getters.isTeacher) {
    throw "Only teacher can add a course";
  }

  await axios.post("/api/teacher/courses", {
    name,
    subjectId
  });
}

export async function deleteCourse(courseId) {
  if (!store.getters.isTeacher) {
    throw "Only teacher can delete a course";
  }

  await axios.delete(`/api/teacher/courses/${courseId}`);
}

export async function addGroupToCourse(courseId, groupId) {
  if (!store.getters.isTeacher) {
    throw "Only teacher can add group to a course";
  }

  await axios.post(`/api/teacher/courses/${courseId}/groups`, {
    groupId
  });
}

export async function removeGroupFromCourse(courseId, groupId) {
  if (!store.getters.isTeacher) {
    throw "Only teacher can delete a groum from a course";
  }

  await axios.delete(`/api/teacher/courses/${courseId}/groups/${groupId}`);
}

export async function queryGroupsInCourse(courseId) {
  const resp = await axios.get(`/api/teacher/courses/${courseId}/groups`);
  return resp.data;
}
