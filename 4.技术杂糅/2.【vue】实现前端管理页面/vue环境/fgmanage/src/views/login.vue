<template>
  <div><!-- @submit="handleSubmit"  -->
    <form @submit.prevent="login" >
      <div class="login">
        <span style="color: #e0002f; transform: translateY(-20px);"></span>
        <div>
          <label>账号</label> <input type="text" class="login-input" v-model="name">
        </div>
        <div>
          <label>密码</label> <input type="password" class="login-input" v-model="password">
        </div>
        <div>
          <button type="submit" class="login-btn">登录</button>
          <button type="reset" class="login-btn">重置</button>
        </div>
      </div>
    </form>
  </div>
</template>

<style type="text/css">

  .login {
  position: fixed;
  left: 50%;
  top: 40%;
  margin: -100px 0px 0px -150px;
  text-align: center;
}

 .login div {
    margin-top: 10px;
  }

.login-btn {
  display: inline-block;
  height: 38px;
  line-height: 38px;
  padding: 0 18px;
  background-color: #009688;
  color: #fff;
  white-space: nowrap;
  text-align: center;
  font-size: 14px;
  border: none;
  border-radius: 2px;
  cursor: pointer;
  margin-right: 10px;
}

.login-input {
  width: 135px;
  padding: 10px 15px;
  border: none;
  background: rgb(242, 242, 242);
  outline: none;
  height: 22px;
  font-size: 16px;
  color: #696969;
  margin-left: 10px;
}
</style>

<script type="text/javascript">
  export default {
    data(){
      return {
        name: '',
        password : ''
      }
    },
    methods: {
      login() {
        this.$router.push({
          name:"/mainPage", 
          params: {
             name: this.name,
             password: this.password
          }})
      }
    }
  };

</script>