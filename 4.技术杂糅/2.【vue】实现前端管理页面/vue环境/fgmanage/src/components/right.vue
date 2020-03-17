<template>
	<div>
		<div v-if="url == '/'">
			<offenuse></offenuse>
		</div>
		<div v-else>
			<userList></userList>
		</div>
	</div>
</template>

<script type="text/javascript">

	import offenuse from './offenuse.vue'
	import userList from './userlist.vue'
	import Msg from './msg.js'
	export default ({
		data(){
			return {"url":'/'}
		},
		mounted: function(){
			var _this = this;
			Msg.$on('getLinkUrl', function(url){
				_this.url = url;
			})
			
		},
		components: {
			offenuse,
			userList
		}
	})
</script>