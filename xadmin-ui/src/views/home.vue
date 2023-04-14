<template>
  <div class="dashboard-container">
    <div class="dashboard-editor-container">
      <panel-group />
    </div>
  </div>
</template>

<script>
import PanelGroup from './dashboard/PanelGroup'
export default {
  name: 'Dashboard',
  components: {
    PanelGroup
  },
  data() {
    return {
    }
  },
  methods: {

  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
</style>
