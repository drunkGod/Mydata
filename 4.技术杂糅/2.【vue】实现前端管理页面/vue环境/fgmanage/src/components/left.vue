<template>
	<div>
		<el-menu>
			<el-submenu index="1">
				<template slot="title"><i class="el-icon-menu"></i>系统管理</template>
				<el-menu-item index="1-1" @click="getLinkUrl('1-1')">用户管理</el-menu-item>
			</el-submenu>
			</el-submenu>
			<el-submenu index="2">
				<template slot="title"><i class="el-icon-menu"></i>常用管理</template>
				<el-submenu index="2-1">
					<template slot="title">选项1</template>
					<el-menu-item index="2-1-1">选项1-1</el-menu-item>
				</el-submenu>
			</el-submenu>
			<el-submenu index="3">
				<template slot="title"><i class="el-icon-menu"></i>其它管理</template>
			</el-submenu>
		</el-menu>
	</div>
</template>

<style type="text/css">
	.hot{
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
			getLinkUrl: function(url){
				Msg.$emit('getLinkUrl', url);
			},

		}
	}
</script>