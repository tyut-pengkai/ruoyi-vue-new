<template>
  <transition-group name="fade-transform" mode="out-in">
    <inner-link
      v-for="(item, index) in iframeViews"
      :key="item.path"
      :iframeId="'iframe' + index"
      v-show="$route.path === item.path"
      :src="iframeUrl(item.meta.link, item.query)"
    ></inner-link>
  </transition-group>
</template>

<script>
import InnerLink from "../InnerLink/index"

export default {
  components: { InnerLink },
  computed: {
    iframeViews() {
      return this.$store.state.tagsView.iframeViews
    }
  },
  methods: {
    iframeUrl(url, query) {
      if (Object.keys(query).length > 0) {
        let params = Object.keys(query).map((key) => {
          let value = this.parseStoreValue(query[key])
          return key + "=" + value
        }).join("&")
        return url + "?" + params
      }
      return url
    },
    parseStoreValue(value){
      // eg. $store.getters.name
      if(value!=null && value.startsWith("$store") && value.indexOf(".")>-1){
        const storeRouter = value.split("\.");
        let temp = this.$store
        for (const index in storeRouter) {
          if (index==0) continue
          const storeRouterKey = storeRouter[index]
          if(storeRouterKey.endsWith("?")){
            const realKey = storeRouterKey.substring(0,storeRouterKey.length-1)
            if(temp[realKey]==null){
              return ""
            }
            temp = temp[realKey]
            continue
          }
          temp = temp[storeRouterKey];
        }
        return temp;
      }
      return value;
    }
  }
}
</script>
