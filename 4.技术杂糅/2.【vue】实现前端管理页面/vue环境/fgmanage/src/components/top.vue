<template>
	<div>
		<img src="favicon.ico"/>
		<span>{{sysName}}</span>
		<span class="top-right">当前用户：{{userName}}</span>
	</div>
</template>

<script type="text/javascript">
	
	export default{
		created(){
			console.log(this.sysName);
			console.log(sessionStorage.getItem('user'));
			if(!sessionStorage.getItem('user')) {
				alert("请登录");
				this.$router.push({path:"/"});
			}
		},
		data(){
	    	return {
	    		sysName: "XXX管理系统",
	    		userName: sessionStorage.getItem('user')
		      }
	    }
	}
</script>