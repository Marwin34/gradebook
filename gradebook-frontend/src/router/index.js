import Vue from "vue";
import VueRouter from "vue-router";

import store from "../store";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "home",
    component: () => import("../views/Home")
  },
  {
    path: "/login",
    name: "login",
    component: () => import("../views/Login"),
    meta: {
      guest: true
    }
  },
  {
    path: "/account",
    name: "account",
    component: () => import("../views/Account")
  },
  {
    path: "/admin",
    name: "admin",
    component: () => import("../views/Admin"),
    meta: {
      admin: true
    }
  },
  {
    path: "/teacher",
    name: "teacher",
    component: () => import("../views/Teacher"),
    meta: {
      teacher: true
    }
  },
  {
    path: "/teacher-courses",
    name: "teacher-courses",
    component: () => import("../views/TeacherCourses"),
    meta: {
      teacher: true
    }
  },
  {
    path: "/teacher-courses/:courseId",
    props: true,
    name: "teacher-course",
    component: () => import("../views/TeacherCourse"),
    meta: {
      teacher: true
    }
  },
  {
    path: "*",
    redirect: "/"
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.guest)) {
    if (store.getters.isAuthenticated) {
      return next("/");
    }

    return next();
  }

  if (!store.getters.isAuthenticated) {
    return next({
      path: "/login",
      query: { redirect: to.fullPath }
    });
  }

  const rules = [
    {
      tag: r => r.meta.admin,
      cond: store.getters.isAdmin
    },
    {
      tag: r => r.meta.teacher,
      cond: store.getters.isTeacher
    }
  ];

  for (const rule of rules) {
    if (to.matched.some(rule.tag) && !rule.cond) {
      return next("/");
    }
  }

  return next();
});

export default router;
