<template>
	<div>
		<GoodList :goodId="k"></GoodList>
	</div>
</template>

<script type="text/javascript">
	import Msg from './msg.js'
	import GoodList from './goodlist.vue'
	export default ({
		data(){
			return {"k":0}
		},
		mounted: function(){
			var _this = this;
			Msg.$on('val', function(v){
				_this.k = v;
			})
		},
		components: {
			GoodList
		}
	})
</script>