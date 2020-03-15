<template>
  <div id="main">
    <div>登录成功了！！！！！！！！！！！！</div>
    <div>login info : {{loginInfo}}</div>

  </div>
</template>

<script type="text/javascript">

	export default {
	    data(){
	    	return {
		        loginInfo: this.$route.params.name+"++"+this.$route.params.password
		      }
	    }
		
	}
	
</script>