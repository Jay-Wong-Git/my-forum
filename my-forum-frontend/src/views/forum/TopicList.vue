<script setup>
import LightCard from "@/components/LightCard.vue";
import {
    Calendar,
    Clock,
    CollectionTag,
    Compass,
    Document,
    Edit,
    EditPen,
    Link,
    Picture,
    Microphone, CircleCheck, Star, FolderOpened, ArrowRightBold
} from "@element-plus/icons-vue";
import Weather from "@/components/Weather.vue";
import {computed, reactive, ref, watch} from "vue";
import {get} from "@/net";
import {ElMessage} from "element-plus";
import TopicEditor from "@/components/TopicEditor.vue";
import {useStore} from "@/store";
import axios from "axios";
import ColorDot from "@/components/ColorDot.vue";
import router from "@/router";
import TopicTag from "@/components/TopicTag.vue";
import TopicCollectList from "@/components/TopicCollectList.vue";

const store = useStore()

// const weather = reactive({
//   location: {},
//   now: {},
//   hourly: [],
//   success: false
// })
const editor = ref(false)
const topics = reactive({
    list: [],
    type: 0,
    page: 0,
    end: false,
    top: []
})
const collects = ref(false)

watch(() => topics.type, () => resetList(), {immediate: true})

const today = computed(() => {
    const date = new Date()
    return `${date.getFullYear()} 年 ${date.getMonth() + 1} 月 ${date.getDate()} 日`
})

get('/api/forum/top-topic', data => topics.top = data)

function updateList() {
    if (topics.end) return
    get(`/api/forum/list-topic?page=${topics.page}&type=${topics.type}`, data => {
        if (data) {
            data.forEach(d => topics.list.push(d))
            topics.page++
        }
        if (!data || data.length < 10)
            topics.end = true
    })
}

function onTopicCreate() {
    editor.value = false
    resetList()
}

function resetList() {
    topics.page = 0
    topics.end = false
    topics.list = []
    updateList()
}

// navigator.geolocation.getCurrentPosition(position => {
//   const longitude = position.coords.longitude
//   const latitude = position.coords.latitude
//   get(`/api/forum/weather?longitude=${longitude}&latitude=${latitude}`, data => {
//     Object.assign(weather, data)
//     weather.success = true
//   })
// }, error => {
//   console.info(error)
//   ElMessage.warning('位置信息获取超时，请检测网络设置')
//   get(`/api/forum/weather?longitude=116.40529&latitude=39.90499`, data => {
//     Object.assign(weather, data)
//     weather.success = true
//   })
// }, {
//   timeout: 3000,
//   enableHighAccuracy: true
// })
</script>

<template>
    <div style="display: flex;margin: 20px auto;gap: 20px;max-width: 1000px">
        <div style="flex: 1">
            <light-card>
                <div class="create-topic" @click="editor = true">
                    <el-icon>
                        <EditPen/>
                    </el-icon>
                    点击发表主题...
                </div>
                <div style="margin-top: 10px;display: flex;gap: 13px;font-size: 18px;color: grey">
                    <el-icon>
                        <Edit/>
                    </el-icon>
                    <el-icon>
                        <Document/>
                    </el-icon>
                    <el-icon>
                        <Compass/>
                    </el-icon>
                    <el-icon>
                        <Picture/>
                    </el-icon>
                    <el-icon>
                        <Microphone/>
                    </el-icon>
                </div>
            </light-card>
            <light-card style="margin-top: 10px;display: flex;flex-direction: column;gap: 10px">
                <div v-for="item in topics.top">
                    <div class="top-topic">
                        <el-tag type="info" size="small">置顶</el-tag>
                        <div class="top-topic-content">
                            <div @click="router.push(`/index/topic-detail/${item.id}`)">
                                {{ item.title }}
                            </div>
                            <div>{{ new Date(item.time).toLocaleString() }}</div>
                        </div>
                    </div>
                    <!--                    <el-divider border-style="dashed" style="margin: 0 auto; opacity: .8;width: 85%"/>-->
                </div>
            </light-card>
            <light-card style="margin-top: 10px;display: flex;gap: 7px">
                <div :class="`type-select-card ${topics.type === item.id ? 'active' : ''}`"
                     v-for="item in store.forum.types"
                     @click="topics.type = item.id">
                    <color-dot :color="item.color"/>
                    <span style="margin-left: 5px">{{ item.name }}</span>
                </div>
            </light-card>
            <transition name="el-fade-in" mode="out-in">
                <div v-if="topics.list.length">
                    <div style="margin-top: 10px;display: flex;flex-direction: column;gap: 10px"
                         v-infinite-scroll="updateList">
                        <light-card v-for="item in topics.list" class="topic-card">
                            <div style="display: flex; position: relative">
                                <div>
                                    <el-avatar :size="30" :src="store.avatarUserUrl(item.avatar)"/>
                                </div>
                                <div style="margin-left: 7px;transform: translateY(-2px)">
                                    <div style="font-size: 13px;font-weight: bold">{{ item.username }}</div>
                                    <div style="font-size: 12px;color: grey">
                                        <el-icon>
                                            <Clock/>
                                        </el-icon>
                                        <div style="margin-left: 2px;display: inline-block;transform: translateY(-2px)">
                                            {{ new Date(item.time).toLocaleString() }}
                                        </div>
                                    </div>
                                </div>
                                <topic-tag style="position: absolute; top: 0; right: 2px;opacity: .7" :type="item.type"/>
                            </div>
                            <el-divider border-style="dashed" style="margin: 0 auto 5px auto; opacity: .7;width: 99%"/>
                            <div class="topic-item"
                                 @click="router.push('/index/topic-detail/'+item.id)">
                                <div style="margin-top: 5px;display: flex">
                                    <span style="font-weight: bold;">{{ item.title }}</span>
                                </div>
                                <div class="topic-content">{{ item.text }}</div>
                                <div style="display: grid;grid-template-columns: repeat(4, 1fr);grid-gap: 10px">
                                    <el-image class="topic-image" v-for="img in item.images" :src="img"
                                              fit="cover"></el-image>
                                </div>
                            </div>
                            <el-divider border-style="dashed"
                                        style="margin: 5px auto -8px auto; opacity: .7;width: 99%"/>
                            <div style="display: flex;gap: 15px;font-size: 13px;margin-top: 10px;opacity: 0.8">
                                <div class="act" style="display:flex; align-items: center; gap: 2px">
                                    <el-icon style="vertical-align: middle">
                                        <CircleCheck/>
                                    </el-icon>
                                    <span>{{ item.like }}&nbsp;点赞</span>
                                </div>
                                <div class="act" style="display:flex; align-items: center; gap: 2px">
                                    <el-icon style="vertical-align: middle">
                                        <Star/>
                                    </el-icon>
                                    <span>{{ item.collect }}&nbsp;收藏</span>
                                </div>
                            </div>
                        </light-card>
                    </div>
                </div>
            </transition>
        </div>
        <div style="width: 280px">
            <div style="position: sticky;top: 20px">
                <light-card>
                    <div class="collect-list-button" @click="collects = true"
                         style="display: flex;justify-content: space-between">
                        <div style="display: flex;align-items: center">
                            <el-icon>
                                <FolderOpened/>
                            </el-icon>
                            <span style="margin-left: 4px">查看我的收藏</span>
                        </div>
                        <el-icon style="transform: translateY(3px)">
                            <ArrowRightBold/>
                        </el-icon>
                    </div>
                </light-card>
                <light-card style="margin-top: 10px">
                    <div style="font-weight: bold">
                        <el-icon>
                            <CollectionTag/>
                        </el-icon>
                        论坛公告
                    </div>
                    <el-divider style="margin: 10px 0"/>
                    <div style="font-size: 14px;margin: 10px;color: grey">
                        月色如水，洒在静谧的湖面上。一叶小舟，在这水墨画般的夜色中悠然荡漾。波光粼粼，繁星点点，倒映在这宁静的湖面，如同置身于一个梦幻的世界。
                    </div>
                </light-card>
                <!--        <light-card style="margin-top: 10px">-->
                <!--          <div style="font-weight: bold">-->
                <!--            <el-icon>-->
                <!--              <Calendar/>-->
                <!--            </el-icon>-->
                <!--            天气信息-->
                <!--          </div>-->
                <!--          <el-divider style="margin: 10px 0"/>-->
                <!--          <weather :data="weather"/>-->
                <!--        </light-card>-->
                <light-card style="margin-top: 10px">
                    <div class="info-text">
                        <div>当前日期</div>
                        <div>{{ today }}</div>
                    </div>
                    <!--          <div class="info-text">-->
                    <!--            <div>当期IP地址</div>-->
                    <!--            <div>127.0.0.1</div>-->
                    <!--          </div>-->
                </light-card>
                <div style="font-size: 14px;margin-top: 10px;color: grey;display: flex;align-items: center">
                    <el-icon>
                        <Link/>
                    </el-icon>
                    <span>友情链接</span>
                </div>
                <el-divider style="margin: 5px 0"/>
                <div style="display: grid;grid-template-columns: repeat(2, 1fr);grid-gap: 10px;margin-top: 10px">
                    <div class="friend-link">
                        <el-image style="height: 100%" src="https://element-plus.org/images/js-design-banner.jpg"/>
                    </div>
                    <div class="friend-link">
                        <el-image style="height: 100%" src="https://element-plus.org/images/vform-banner.png"/>
                    </div>
                    <div class="friend-link">
                        <el-image style="height: 100%" src="https://element-plus.org/images/sponsors/jnpfsoft.jpg"/>
                    </div>
                </div>
            </div>
        </div>
        <topic-editor :show="editor" @success="onTopicCreate" @close="editor = false"/>
        <topic-collect-list :show="collects" @close="collects = false"/>
    </div>
</template>

<style lang="less" scoped>
.collect-list-button {
    font-size: 14px;
    display: flex;
    justify-content: space-between;
    transition: .3s;

    &:hover {
        cursor: pointer;
        opacity: 0.6;
    }
}

.top-topic {
    display: flex;

    .top-topic-content {
        flex: 1;
        display: flex;
        border-bottom: dashed 1px rgba(220, 223, 230, .8);
        margin: 0 10px;

        div:first-of-type {
            font-size: 14px;
            font-weight: bold;
            opacity: 0.6;
            transition: opacity .3s;

            &:hover {
                opacity: .4;
                cursor: pointer;
            }
        }

        div:nth-of-type(2) {
            flex: 1;
            color: grey;
            font-size: 13px;
            text-align: right;
        }
    }
}

.dark {
    .top-topic {
        .top-topic-content {
            border-bottom: dashed 1px rgba(76, 77, 79, .8);
        }
    }
}

.type-select-card {
    background-color: #f5f5f5;
    padding: 2px 7px;
    font-size: 14px;
    border-radius: 3px;
    box-sizing: border-box;
    transition: background-color .3s;

    &.active {
        border: solid 1px #ead4c4;
    }

    &:hover {
        cursor: pointer;
        background-color: #dadada;
    }
}

.topic-card {
    padding: 15px 15px 10px;
    transition: scale .3s;

    &:hover {
        scale: 1.015;
        //cursor: pointer;
    }

    .topic-item {
        min-height: 80px;

        &:hover {
            cursor: pointer;
        }

        .topic-content {
            font-size: 13px;
            color: grey;
            margin: 10px 0 5px 0;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 3;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .topic-image {
            //width: 100%;
            //height: 100%;
            max-height: 80px;
            border-radius: 5px;
        }
    }
}

.info-text {
    display: flex;
    justify-content: space-between;
    color: grey;
    font-size: 14px;
}

.friend-link {
    border-radius: 5px;
    overflow: hidden;
}

.create-topic {
    background-color: #efefef;
    border-radius: 5px;
    height: 40px;
    color: grey;
    font-size: 14px;
    line-height: 40px;
    padding: 0 10px;

    &:hover {
        cursor: pointer;
    }
}

.dark {
    .create-topic {
        background-color: #232323;
    }

    .type-select-card {
        background-color: #282828;

        &.active {
            border: solid 1px #64594b;
        }

        &:hover {
            background-color: #5e5e5e;
        }
    }
}
</style>
