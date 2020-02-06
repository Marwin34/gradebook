import axios from "axios";
import store from "../store";

export async function addAssignment(courseId, name) {
  if (!store.getters.isTeacher) {
    throw "Only teacher can add an assignment";
  }

  await axios.post(`/api/teacher/courses/${courseId}/assignments`, {
    name
  });
}

export async function removeAssignment(courseId, assignmentId) {
  if (!store.getters.isTeacher) {
    throw "Only teacher can delete an assignment";
  }

  await axios.delete(
    `/api/teacher/courses/${courseId}/assignments/${assignmentId}`
  );
}

export async function queryAssignment(courseId, assignmentId) {
  const resp = await axios.get(
    `/api/teacher/courses/${courseId}/assignments/${assignmentId}`
  );
  return resp.data;
}

export async function addGroupToAssignment(courseId, assignmentId, groupId) {
  if (!store.getters.isTeacher) {
    throw "Only teacher can add group to an assignment";
  }

  await axios.post(
    `/api/teacher/courses/${courseId}/assignments/${assignmentId}/groups`,
    {
      groupId
    }
  );
}

export async function removeGroupFromAssignment(
  courseId,
  assignmentId,
  groupId
) {
  if (!store.getters.isTeacher) {
    throw "Only teacher can delete a group from an assignment";
  }

  await axios.delete(
    `/api/teacher/courses/${courseId}/assignments/${assignmentId}/groups/${groupId}`
  );
}

export async function queryGroupsInAssignment(courseId, assignmentId) {
  const resp = await axios.get(
    `/api/teacher/courses/${courseId}/assignments/${assignmentId}/groups`
  );
  return resp.data;
}
