<template>
	<div>
		<!-- <GoodList :goodId="k"></GoodList> -->
		<el-row>
		  <el-button>默认按钮</el-button>
		  <el-button type="primary">主要按钮</el-button>
		  <el-button type="success">成功按钮</el-button>
		  <el-button type="info">信息按钮</el-button>
		  <el-button type="warning">警告按钮</el-button>
		  <el-button type="danger">危险按钮</el-button>
		</el-row>
	</div>
</template>

<script type="text/javascript">
	// import Msg from './msg.js'
	// import GoodList from './goodlist.vue'
	export default ({
		data(){
			return {"k":0}
		},
		mounted: function(){
			// var _this = this;
			// Msg.$on('val', function(v){
			// 	_this.k = v;
			// })
		},
		components: {
			// GoodList
		}
	})
</script>