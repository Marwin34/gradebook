import axios from "axios";
import store from "../store";

export async function queryStudents() {
  if (!store.getters.isTeacher) {
    throw "Only teacher is able to query students";
  }

  const resp = await axios.get("/api/teacher/students");
  return resp.data;
}

export async function addStudent(email, firstName, lastName) {
  if (!store.getters.isTeacher) {
    throw "Only teacher is able to add a student";
  }

  await axios.post("/api/teacher/students", {
    email,
    firstName,
    lastName
  });
}

export async function deleteStudent(studentId) {
  if (!store.getters.isTeacher) {
    throw "Only teacher is able to delete a student";
  }

  await axios.delete(`/api/teacher/students/${studentId}`);
}
