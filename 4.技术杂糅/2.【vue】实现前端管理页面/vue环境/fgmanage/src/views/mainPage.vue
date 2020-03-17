<template>
	<div class="main">
		<!-- 顶部 -->
		<div class="top">
			<top></top>
		</div>
		<!-- 左侧菜单 -->
		<div class="left">
			<left></left>
		</div>
		<!-- 右侧导航 -->
		<div>
			
		</div>
		<!-- 右侧内容 -->
		<div class="right">
			<linktab></linktab>
			<right></right>
		</div>
	</div>


</template>

<style>
	@import '../assets/mainPage.css'
</style>

<script type="text/javascript">
	import top from "../components/top.vue"
	import left from "../components/left.vue"
	import right from "../components/right.vue"
	import linktab from '../components/linktab.vue'

	export default{
		components: {
			top,
			left,
			right,
			linktab
		}
	}
	
</script>

