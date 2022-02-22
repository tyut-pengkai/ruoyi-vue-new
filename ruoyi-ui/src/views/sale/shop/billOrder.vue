<template>
    <div>
        4567890-
        订单号：{{orderId}}
    </div>
</template>>

<script>
export default {
    data() {
        return {
            orderId: null,
        }
    },
    created() {
        this.orderId = this.$route.query && this.$route.query.orderId;
    },
}
</script>

<style scoped>

</style>