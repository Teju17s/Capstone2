<template>
  <div id="app">
    <header class="app-header">
      <h1>Digital FD System</h1>
      <nav>
        <router-link to="/book-fd" class="nav-link">Book FD</router-link>
        <router-link to="/fd-list" class="nav-link">My FDs</router-link>
      </nav>
    </header>

    <main class="app-main">
      <router-view></router-view>
    </main>
  </div>
</template>

<script>
export default {
  name: 'App'
};
</script>

<style scoped>
#app {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #333;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f5f5;
}

.app-header {
  background: #1890ff;
  color: white;
  padding: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

nav .nav-link {
  margin-left: 16px;
  color: white;
  text-decoration: none;
  font-weight: 500;
}

nav .nav-link.router-link-active {
  text-decoration: underline;
}

.app-main {
  flex: 1;
  padding: 20px;
  display: flex;
  justify-content: center;
}
</style>
