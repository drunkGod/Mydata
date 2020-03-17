<template>
	<div>
		<el-tabs v-model="editableTabsValue" type="card" editable @edit="handleTabsEdit">
		  <el-tab-pane
		    :key="item.name"
		    v-for="(item, index) in editableTabs"
		    :label="item.title"
		    :name="item.name"
		  >
		    {{item.content}}
		  </el-tab-pane>
		</el-tabs>
	</div>
</template>

<script>
  import Msg from './msg.js'

  export default {
    data() {
      return {
        editableTabsValue: '1',
        editableTabs: [{
          title: '主页',
          name: '1',
          content: ''
        }, {
          title: '用户管理',
          name: '2',
          content: ''
        }],
        tabIndex: 2
      }
    },
    methods: {
      handleTabsEdit(targetName, action) {
        if (action === 'add') {
          let newTabName = ++this.tabIndex + '';
          this.editableTabsValue.push({
            title: 'New Tab',
            name: newTabName,
            content: 'New Tab content'
          });
          this.editableTabsValue = newTabName;
        }
        if (action === 'remove') {
          let tabs = this.editableTabs;
          let activeName = this.editableTabsValue;
          if(activeName === '1') {
            Msg.$emit('getLinkUrl', '/');
            return;
          }
          if (activeName === targetName) {
            tabs.forEach((tab, index) => {
              if (tab.name === targetName) {
                let nextTab = tabs[index + 1] || tabs[index - 1];
                if (nextTab) {
                  activeName = nextTab.name;
                }
              }
            });
          }
          
          this.editableTabsValue = activeName;
          this.editableTabs = tabs.filter(tab => tab.name !== targetName);
        }
      }
    }
  }
</script>