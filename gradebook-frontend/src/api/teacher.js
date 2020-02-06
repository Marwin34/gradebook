import axios from "axios";
import store from "../store";

export async function queryTeachers() {
  if (!store.getters.isAdmin) {
    throw "Only administrator is able to query teachers";
  }

  const resp = await axios.get("/api/teachers");
  return resp.data;
}

export async function addTeacher(email, firstName, lastName) {
  if (!store.getters.isAdmin) {
    throw "Only administrator is able to add teachers";
  }

  const resp = await axios.post("/api/teachers", {
    email,
    firstName,
    lastName
  });

  return resp.data.password;
}

export async function deleteTeacher(teacherId) {
  if (!store.getters.isAdmin) {
    throw "Only administrator is able to remove a teacher";
  }

  await axios.delete(`/api/teachers/${teacherId}`);
}
