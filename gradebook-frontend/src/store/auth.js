import axios from "axios";

import router from "../router";

export default {
  state: {
    token: "",
    refresh: "",
    expiration: null,
    email: "",
    authorities: []
  },

  getters: {
    isAuthenticated: state => !!state.token,
    token: state => state.token,
    refresh: state => state.refresh,
    expiration: state => state.expiration,
    email: state => state.email,
    isAdmin: state => state.authorities.includes("ROLE_ADMIN"),
    isTeacher: state => state.authorities.includes("ROLE_TEACHER"),
    isParent: state => state.authorities.includes("ROLE_PARENT"),
    isStudent: state => state.authorities.includes("ROLE_STUDENT")
  },

  mutations: {
    auth_login(state, { token, refresh, expiration, email, authorities }) {
      state.token = token;
      state.refresh = refresh;
      state.expiration = expiration;
      state.email = email;
      state.authorities = authorities;
    },

    auth_logout(state) {
      state.token = "";
      state.refresh = "";
      state.expiration = null;
      state.email = "";
      state.authorities = [];
    },

    auth_refresh(state, { token, refresh, expiration }) {
      state.token = token;
      state.refresh = refresh;
      state.expiration = expiration;
    },

    auth_email_authorities(state, { email, authorities }) {
      state.email = email;
      state.authorities = authorities;
    }
  },

  actions: {
    async auth_login({ commit, dispatch }, { email, password }) {
      const resp = await axios.post("api/auth/login", { email, password });

      const token = resp.data.accessToken.token;
      const refresh = resp.data.refreshToken;
      const expiration = resp.data.accessToken.expirationDate;
      const authorities = resp.data.authorities;
      commit("auth_login", { token, refresh, expiration, email, authorities });
      dispatch("current_user");
    },

    async auth_logout({ commit }) {
      commit("auth_logout");
      router.push("/login");
    }
  }
};
