<template>
	<div>
		<div class="tuijian" @click="toLinkUrl('hot')">热门推荐</div>
		<ul class="menu">
			<li @click="toLinkUrl(1)">链接1</li>
			<li @click="toLinkUrl(2)">链接2</li>
			<li @click="toLinkUrl(3)">链接3</li>
			<li @click="toLinkUrl(4)">链接4</li>
			<li @click="toLinkUrl(5)">链接5</li>
			<li @click="toLinkUrl(6)">链接6</li>
			<li @click="toLinkUrl(7)">链接7</li>
			<li @click="toLinkUrl(8)">链接8</li>
			<li @click="toLinkUrl(9)">链接9</li>
			<li @click="toLinkUrl(10)">链接10</li>
			<li @click="toLinkUrl(11)">链接11</li>
			<li @click="toLinkUrl(12)">链接12</li>
			<li @click="toLinkUrl(13)">链接13</li>
		</ul>
	</div>
</template>

<style type="text/css">
	.tuijian{
		color: red;
	}
	.menu li{
		list-style: none;
		height: 35px;
		line-height: 35px;
		background-color: white;
		margin-bottom: 2px; 
		cursor:pointer;
	}
</style>

<script type="text/javascript">
	import Msg from "./msg.js"
	export default{
		methods: {
			toLinkUrl: function(url){
				Msg.$emit('val', url);
			},

		}
	}
</script>