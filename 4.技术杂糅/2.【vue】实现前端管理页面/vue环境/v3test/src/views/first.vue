<template>
	<div class="main">
		<div class="left">
			<left></left>
		</div>
		<div class="right">
			<div class="right-top">
				<img src="favicon.ico"></img>
			</div>
			<div class="right-bottom">
				<right></right>
			</div>
		</div>
	</div>

	
</template>

<script>
	import left from "../components/left.vue"
	import right from "../components/right.vue"
	export default{
		components: {
			left,
			right
		}
	}
	
</script>

<style>
	@import '../assets/first.css'
</style>