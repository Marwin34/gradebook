module.exports = {
  transpileDependencies: ["vuetify"],
  devServer: {
    proxy: {
      "/api": {
        target: "http://localhost:9090",
        ws: true,
        changeOrigin: true
      }
    }
  }
};
