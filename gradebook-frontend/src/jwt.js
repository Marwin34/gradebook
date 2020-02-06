import axios from "axios";
import refreshAuth from "axios-auth-refresh";

import store from "./store";

function getToken() {
  const token = store.getters.token;
  return `Bearer ${token}`;
}

export function configureAxios() {
  axios.interceptors.request.use(request => {
    request.headers["Authorization"] = getToken();
    return request;
  });

  refreshAuth(axios, refreshToken);
}

async function refreshToken(failedRequest) {
  if (failedRequest.response.config.url.startsWith("api/auth")) {
    throw failedRequest.response;
  }

  const email = store.getters.email;
  const refreshToken = store.getters.refresh;

  if (!email || !refreshToken) {
    throw "Refresh token is not saved in local storage";
  }

  try {
    const resp = await axios.post("api/auth/refresh-token", {
      email,
      refreshToken
    });

    const token = resp.data.token.token;
    const refresh = resp.data.refreshToken;
    const expiration = resp.data.token.expirationDate;

    store.commit("auth_refresh", { token, refresh, expiration });

    failedRequest.response.config.headers["Authorization"] = getToken();

    console.log("Refreshed JWT token.");
  } catch (error) {
    await store.dispatch("auth_logout");
    throw `Unable to refresh token: ${error.response}`;
  }
}
