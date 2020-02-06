import axios from "axios";
import store from "../store";

export async function querySubjects() {
  const resp = await axios.get("/api/subjects");
  return resp.data;
}

export async function addSubject(name) {
  if (!store.getters.isAdmin) {
    throw "Only admin is able to add subject";
  }

  await axios.post("/api/subjects", {
    name
  });
}

export async function deleteSubject(subjectId) {
  if (!store.getters.isAdmin) {
    throw "Only admin is able to remove a subject";
  }

  await axios.delete(`/api/subjects/${subjectId}`);
}
