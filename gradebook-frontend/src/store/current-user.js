import axios from "axios";

export default {
  state: {
    firstName: "",
    lastName: ""
  },

  getters: {
    fullName: state => `${state.firstName} ${state.lastName}`
  },

  mutations: {
    current_user(state, { firstName, lastName }) {
      state.firstName = firstName;
      state.lastName = lastName;
    }
  },

  actions: {
    async current_user({ commit, dispatch }) {
      try {
        const resp = await axios.get("/api/current-user");
        const firstName = resp.data.firstName;
        const lastName = resp.data.lastName;
        const email = resp.data.email;
        const authorities = resp.data.authorities;

        commit("current_user", { firstName, lastName });
        commit("auth_email_authorities", { email, authorities });
      } catch (error) {
        console.error(`Unable to fetch current user: ${error.statusText}`);
        dispatch("auth_logout");
      }
    }
  }
};
