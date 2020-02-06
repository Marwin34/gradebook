import axios from "axios";
import store from "../store";

export async function queryGroups() {
  const resp = await axios.get("/api/teacher/groups");
  return resp.data;
}

export async function addGroup(name) {
  if (!store.getters.isTeacher) {
    throw "Only teacher can add a group";
  }

  await axios.post("/api/teacher/groups", {
    name
  });
}

export async function deleteGroup(groupId) {
  if (!store.getters.isTeacher) {
    throw "Only teacher can delete a group";
  }

  await axios.delete(`/api/teacher/groups/${groupId}`);
}

export async function queryGroup(groupId) {
  const resp = await axios.get(`/api/teacher/groups/${groupId}`);
  return resp.data;
}

export async function addStudentToGroup(studentId, groupId) {
  if (!store.getters.isTeacher) {
    throw "Only teacher can add student to a group";
  }

  await axios.post(`/api/teacher/groups/${groupId}/students`, {
    studentId
  });
}

export async function removeStudentFromGroup(studentId, groupId) {
  if (!store.getters.isTeacher) {
    throw "Only teacher can remove student from a group";
  }

  await axios.delete(`/api/teacher/groups/${groupId}/students/${studentId}`);
}
