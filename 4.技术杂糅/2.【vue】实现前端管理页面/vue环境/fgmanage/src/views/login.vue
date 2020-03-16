<template>
  <div>
    <div class="login">
      <div v-if="err != ''">
        <span style="color: #e0002f; transform: translate(-50px,-30px); position: fixed;" >{{err}}</span>
      </div>
      <div>
        <input type="text" class="login-input" placeholder="用户名" v-model="name">
      </div>
      <div>
        <input type="password" class="login-input" placeholder="密码" v-model="password">
      </div>
      <div>
        <button class="login-btn" @click="login">登录</button>
        <button class="login-btn" @click="resetParam">重置</button>
      </div>
    </div>
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
        password : '',
        err : ''
      }
    },
    methods: {
      login() {
        if(this.name == '') {
          this.err = '用户名不能为空';
          return;
        }
        this.$router.push({
          name:"/mainPage", 
          params: {
             name: this.name,
             password: this.password
          }})
      },
      resetParam() {
        this.name = '';
        this.password = '';
        this.err = '';
      }
    }
  };

</script>