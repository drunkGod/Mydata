<template>
	<div  name="show">
		<div v-if="list.length == 0">加载中。。。</div>
		<ul class="list">
			<li v-for="good in list">
				<img v-bind:src="good.url">
				<p>{{good.name}}</p>
			</li>
		</ul>

	</div>
</template>

<style type="text/css">
	.list li{
		width: 200px;
		height: 200px;
		float: left;
		font-size: 9px;
	}

	.list img{
		width: 180px;
		height: 180px;
	}

</style>

<script type="text/javascript">
	export default{
		name: "show",
		data(){
			var _this = this;
			this.$http.get("data.json").then(function(res){
				_this.list = res.data.dn;
			});
			setTimeout("alert(11);",1000);
			return{
				list: []
			}
		},
		props:{
			goodId: Number
		},
		watch:{
			goodId(){
				var obj = this;
				var url = "data.json";
				this.$http.get(url).then(function(res){
					console.log(res);
					if(obj.goodId==1){
					 	obj.list = res.data.sj;

					} else {
						obj.list = res.data.dn;

					}
				})
			}
		}
	}
</script> 

