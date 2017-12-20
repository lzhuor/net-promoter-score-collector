<template>
    <div class="container">
        <div class="footer-main">
            <p class="footer-main-title">{{ message }}</p>
            <a href="https://github.com/lzhuor" target="_blank" class="footer-main-link">Github</a>
            <a href="mailto:zhuoran666@gmail.com" class="footer-main-link">Email</a>
        </div>

        <div class="layout-copy footdoc">

            <i class="footdoc">
                <canvas id="myCanvas" width="80" height="80" style="background-color: transparent;"></canvas>
            </i>
            <i class="footdoc">GitHub@lzhuor John Li</i>
        </div>
    </div>

</template>

<script>

    export default {
        data(){
            return {
                message: 'Dashboard made with ❤️',
            }
        },
        mounted (){

            const {Stage, Curve, motion} = this.$curvejs;

            let canvas = document.getElementById('myCanvas'),
                stage = new Stage(canvas);

            let curve = new Curve({
                color: '#ffaeac',
                data: {value: 0, step: 0.01, width: 80, height: 80},
                motion: motion.noise
            });
            stage.add(curve);

            function tick() {

                stage.update();
                requestAnimationFrame(tick)
            }

            tick()
        },

    }
</script>

<style scoped>
    .container {
        width: 1140px;
        padding: 0 30px;
        margin: 0 auto;
    }

    a {
        text-decoration: none;
    }

    .footer-main {
        font-size: 0;
        padding-top: 40px;
        display: inline-block;
    }

    .footer-main .footer-main-title {
        line-height: 1;
        font-size: 22px;
        margin: 0;
    }

    .footer-main .footer-main-link {
        display: inline-block;
        margin: 12px 18px 0 0;
        line-height: 1;
        font-size: 12px;
        color: #768193;
    }

    .layout-copy {
        display: inline-block;
        width: 570px;
        height: 70px;
        margin-left: 3px;
    }

    .layout-copy .footdoc {
        display: inline-block;
        text-align: center;
        color: #8d99ab;
        width: 570px;
        vertical-align: middle;
        font-size: 12px;
    }

    .footer-social {
        float: right;
        font-size: 0;
        padding: 0 30px;
    }

    .footdoc span {
        display: inline-block;
        font-size: 14px;
        color: #768193;

    }

    .footdoc img {
        display: inline-block;
        width: 84px;
        height: 80px;
        margin-top: 20px;
        transform:rotate(330deg);
        -ms-transform:rotate(330deg); /* Internet Explorer */
        -moz-transform:rotate(330deg); /* Firefox */
        -webkit-transform:rotate(330deg); /* Safari 和 Chrome */
        -o-transform:rotate(330deg); /* Opera */

    }


</style>