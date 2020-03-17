<template>
	<div>
		<div class="fixcenter">
				<div class="transition-box-div">
			      <transition name="el-fade-in-linear">
			        <div v-show="show" class="transition-box">常用链接1</div>
			      </transition>
			      <transition name="el-fade-in">
			        <div v-show="show" class="transition-box">常用链接2</div>
			      </transition>
			      <transition name="el-fade-in-linear">
			        <div v-show="show" class="transition-box">常用链接3</div>
			      </transition>
			    </div>
			    <div class="transition-box-div">
			      <transition name="el-fade-in-linear">
			        <div v-show="show" class="transition-box">常用链接1</div>
			      </transition>
			      <transition name="el-fade-in">
			        <div v-show="show" class="transition-box">常用链接2</div>
			      </transition>
			      <transition name="el-fade-in-linear">
			        <div v-show="show" class="transition-box">常用链接3</div>
			      </transition>
			    </div>
			    <div class="transition-box-div">
			      <transition name="el-fade-in-linear">
			        <div v-show="show" class="transition-box">常用链接1</div>
			      </transition>
			      <transition name="el-fade-in">
			        <div v-show="show" class="transition-box">常用链接2</div>
			      </transition>
			      <transition name="el-fade-in-linear">
			        <div v-show="show" class="transition-box">常用链接3</div>
			      </transition>
			    </div>
		</div>
	</div>
</template>

<script>
    export default {
	    data() {
	      return {
	      	show : false,
	        activeNames: ['1']
	      };
	    },
	    mounted(){
	    	setTimeout(() => {
			   this.show = true;//娃娃消失
			}, 600);
	    }
  	}
</script>

<style>
  .fixcenter{
	position: fixed;
    left: 25%;
    top: 25%;
    /*margin: -100px 0px 0px -150px;*/
    text-align: center;
  }

  .transition-box-div {
  	display: flex; 
  	margin-left: 120px; 
  	margin-top: 20px; 
  	height: 100px;
  }

  .transition-box {
    margin-bottom: 10px;
    width: 200px;
    height: 100px;
    border-radius: 4px;
    background-color: #E9EEF3;
    text-align: center;
    color: #000;
    padding: 40px 20px;
    box-sizing: border-box;
    margin-right: 20px;
 	font-size: 16px;
  }
</style>