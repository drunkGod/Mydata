<template>
	<div class="main">
		<!-- 顶部 -->
		<div class="top">
			<span>{{sysName}}</span>
			<span class="top-right">当前用户：{{userName}}</span>
		</div>
		<!-- 左侧菜单 -->
		<div class="left">
			<leftMenu></leftMenu>
		</div>
		<!-- 右侧内容 -->
		<div class="right">
			<right></right>
		</div>
	</div>


</template>

<style>
	@import '../assets/mainPage.css'
</style>

<script type="text/javascript">
	import leftMenu from "../components/leftMenu.vue"
	import right from "../components/right.vue"
	export default{
		created(){
			console.log(this.sysName);
			console.log(this.userName);
			if(this.userName == undefined) {
				alert("请登录");
				this.$router.push({path:"/"});
			}
		},
		data(){
	    	return {
	    		sysName: "XXXX管理系统",
	    		userName: this.$route.params.name
		      }
	    },
		components: {
			leftMenu,
			right
		}
	}
	
</script>

