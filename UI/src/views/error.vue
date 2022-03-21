<template>
    <div class="login_container">
        <div>
            <img src="../assets/imgs/404.jpg" width="659px" height="346px">
        </div>
        <div>
            <span>你所访问的页面不存在！<span v-on:click="goOff" style="color: blue;cursor:pointer;">点击这里</span>返回上一页</span>
        </div>
    </div>
</template>

<script>
export default {
    name: "error.vue",
    methods: {
        goOff() {
            this.$router.go(-1);
        },
    }
}
</script>

<style scoped>
.login_container {
    margin: 0;
    padding: 0;
    /*background-image: url("src/assets/imgs/bg.jpg");*/
    background-color: #f7f7f7;
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center center; /* 背景图垂直、水平均居中 */
    background-attachment: fixed;
    /*background-color: #2b4b6b;*/
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}
</style>