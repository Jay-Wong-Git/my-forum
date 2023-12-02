<script setup>
import {get, logout} from "@/net";
import router from "@/router";
import {useStore} from "@/store";
import {reactive, ref} from "vue";
import {
    Back,
    Bell,
    ChatDotSquare, Check, Collection, DataLine,
    Document, Files,
    Location, Lock, Message, Monitor,
    Notification, Operation,
    Position,
    School, Search,
    Umbrella, User
} from "@element-plus/icons-vue";
import LightCard from "@/components/LightCard.vue";

const store = useStore();
const loading = ref(true);
const searchInput = reactive({
    type: '1',
    text: ''
})

const notification = ref([])

const loadNotification = () =>
    get('/api/notification/list', data => notification.value = data)

loadNotification()

function confirmNotification(id, url) {
    get(`/api/notification/delete?id=${id}`, () => {
        loadNotification()
        router.push(url)
        // window.open(url)
    })
}

function deleteAllNotification() {
    get(`/api/notification/delete-all`, loadNotification)
}

get("/api/user/info", (data) => {
    store.user = data;
    loading.value = false;
})

function userLogout() {
    logout(() => router.push("/"))
}


</script>

<template>
    <div class="main-content" v-loading="loading" element-loading-text="Loading, wait a second...">
        <el-container style="height: 100%" v-if="!loading">
            <el-header class="main-content-header">
                <!--                <el-image class="logo" src="https://element-plus.org/images/element-plus-logo.svg"/>-->
                <el-image class="logo" src="/logo-site.png" @click="router.push('/')"/>
                <div style="flex: 1;padding: 0 20px 0 60px;text-align: center;margin-right: 110px">
                    <el-input v-model="searchInput.text" style="width: 100%;max-width: 600px"
                              placeholder="搜索论坛相关内容...">
                        <template #prefix>
                            <el-icon>
                                <Search/>
                            </el-icon>
                        </template>
                        <template #prepend>
                            <el-select v-model="searchInput.type" style="width: 110px">
                                <el-option value="1" label="帖子广场"/>
                                <el-option value="2" label="校园活动"/>
                                <el-option value="3" label="表白墙"/>
                                <el-option value="4" label="教务通知"/>
                            </el-select>
                        </template>
                    </el-input>
                </div>
                <div class="user-info">
                    <el-popover placement="bottom" :width="350" trigger="click">
                        <template #reference>
                            <el-badge style="margin-right: 15px" is-dot :hidden="!notification.length">
                                <div class="notification">
                                    <el-icon>
                                        <Bell/>
                                    </el-icon>
                                    <div style="font-size: 10px">消息</div>
                                </div>
                            </el-badge>
                        </template>
                        <el-empty :image-size="80" description="暂时没有未读消息哦~" v-if="!notification.length"/>
                        <el-scrollbar :max-height="500" v-else>
                            <light-card v-for="item in notification" class="notification-item"
                                        @click="confirmNotification(item.id, item.url)">
                                <div>
                                    <el-tag size="small" :type="item.type">消息</el-tag>&nbsp;
                                    <span style="font-weight: bold">{{ item.title }}</span>
                                </div>
                                <el-divider style="margin: 7px 0 3px 0"/>
                                <div style="font-size: 13px;color: grey">
                                    {{ item.content }}
                                </div>
                            </light-card>
                        </el-scrollbar>
                        <div style="margin-top: 10px">
                            <el-button size="small" type="info" :icon="Check" @click="deleteAllNotification"
                                       style="width: 100%" plain>清除全部未读消息
                            </el-button>
                        </div>
                    </el-popover>
                    <div class="profile">
                        <div>{{ store.user.username }}</div>
                        <div>{{ store.user.email }}</div>
                    </div>
                    <el-dropdown>
                        <el-avatar :src="store.avatarUrl"/>
                        <template #dropdown>
                            <el-dropdown-item @click="router.push('/index/user-setting')">
                                <el-icon>
                                    <Operation/>
                                </el-icon>
                                个人设置
                            </el-dropdown-item>
                            <el-dropdown-item>
                                <el-icon>
                                    <Message/>
                                </el-icon>
                                消息列表
                            </el-dropdown-item>
                            <el-dropdown-item @click="userLogout" divided>
                                <el-icon>
                                    <Back/>
                                </el-icon>
                                退出登录
                            </el-dropdown-item>
                        </template>
                    </el-dropdown>
                </div>
            </el-header>
            <el-container>
                <el-aside width="230px">
                    <el-scrollbar style="height: calc(100vh - 55px)">
                        <el-menu
                            router
                            :default-active="$route.path"
                            :default-openeds="['1','2','3']"
                            style="min-height: calc(100vh - 55px)">
                            <el-sub-menu index="1">
                                <template #title>
                                    <el-icon>
                                        <Location/>
                                    </el-icon>
                                    <span><b>校园论坛</b></span>
                                </template>
                                <el-menu-item index="/index">
                                    <template #title>
                                        <el-icon>
                                            <ChatDotSquare/>
                                        </el-icon>
                                        帖子广场
                                    </template>
                                </el-menu-item>
                                <el-menu-item disabled>
                                    <template #title>
                                        <el-icon>
                                            <Bell/>
                                        </el-icon>
                                        失物招领
                                        <el-tag style="margin-left: 10px" size="small" effect="plain" type="info" round>
                                            敬请期待
                                        </el-tag>
                                    </template>
                                </el-menu-item>
                                <el-menu-item disabled>
                                    <template #title>
                                        <el-icon>
                                            <Notification/>
                                        </el-icon>
                                        校园活动
                                        <el-tag style="margin-left: 10px" size="small" effect="plain" type="info" round>
                                            敬请期待
                                        </el-tag>
                                    </template>
                                </el-menu-item>
                                <el-menu-item disabled>
                                    <template #title>
                                        <el-icon>
                                            <Umbrella/>
                                        </el-icon>
                                        表白墙
                                        <el-tag style="margin-left: 10px" size="small" effect="plain" type="info" round>
                                            敬请期待
                                        </el-tag>
                                    </template>
                                </el-menu-item>
                                <el-menu-item disabled>
                                    <template #title>
                                        <el-icon>
                                            <School/>
                                        </el-icon>
                                        海文考研
                                        <el-tag style="margin-left: 10px" size="small" effect="plain" type="info" round>
                                            敬请期待
                                        </el-tag>
                                    </template>
                                </el-menu-item>
                            </el-sub-menu>
                            <el-sub-menu index="2">
                                <template #title>
                                    <el-icon>
                                        <Position/>
                                    </el-icon>
                                    <span><b>探索与发现</b></span>
                                </template>
                                <el-menu-item disabled>
                                    <template #title>
                                        <el-icon>
                                            <Document/>
                                        </el-icon>
                                        成绩查询
                                        <el-tag style="margin-left: 10px" size="small" effect="plain" type="info" round>
                                            敬请期待
                                        </el-tag>
                                    </template>
                                </el-menu-item>
                                <el-menu-item disabled>
                                    <template #title>
                                        <el-icon>
                                            <Files/>
                                        </el-icon>
                                        班级课程表
                                        <el-tag style="margin-left: 10px" size="small" effect="plain" type="info" round>
                                            敬请期待
                                        </el-tag>
                                    </template>
                                </el-menu-item>
                                <el-menu-item disabled>
                                    <template #title>
                                        <el-icon>
                                            <Monitor/>
                                        </el-icon>
                                        教务通知
                                        <el-tag style="margin-left: 10px" size="small" effect="plain" type="info" round>
                                            敬请期待
                                        </el-tag>
                                    </template>
                                </el-menu-item>
                                <el-menu-item disabled>
                                    <template #title>
                                        <el-icon>
                                            <Collection/>
                                        </el-icon>
                                        在线图书馆
                                        <el-tag style="margin-left: 10px" size="small" effect="plain" type="info" round>
                                            敬请期待
                                        </el-tag>
                                    </template>
                                </el-menu-item>
                                <el-menu-item disabled>
                                    <template #title>
                                        <el-icon>
                                            <DataLine/>
                                        </el-icon>
                                        预约教室
                                        <el-tag style="margin-left: 10px" size="small" effect="plain" type="info" round>
                                            敬请期待
                                        </el-tag>
                                    </template>
                                </el-menu-item>
                            </el-sub-menu>
                            <el-sub-menu index="3">
                                <template #title>
                                    <el-icon>
                                        <Operation/>
                                    </el-icon>
                                    <span><b>个人设置</b></span>
                                </template>
                                <el-menu-item index="/index/user-setting">
                                    <template #title>
                                        <el-icon>
                                            <User/>
                                        </el-icon>
                                        个人信息设置
                                    </template>
                                </el-menu-item>
                                <el-menu-item index="/index/privacy-setting">
                                    <template #title>
                                        <el-icon>
                                            <Lock/>
                                        </el-icon>
                                        账号安全设置
                                    </template>
                                </el-menu-item>
                            </el-sub-menu>
                        </el-menu>
                    </el-scrollbar>
                </el-aside>
                <el-main class="main-content-page">
                    <el-scrollbar style="height: calc(100vh - 55px)">
                        <router-view v-slot="{ Component }">
                            <transition name="el-fade-in-linear" mode="out-in">
                                <component :is="Component" style="height: 100%"/>
                            </transition>
                        </router-view>
                    </el-scrollbar>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<style lang="less" scoped>
.notification-item {
    transition: .3s;

    &:hover {
        cursor: pointer;
        opacity: 0.7;
    }
}

.notification {
    font-size: 22px;
    line-height: 14px;
    text-align: center;
    transition: color .3s;

    &:hover {
        color: grey;
        cursor: pointer;
    }
}

.main-content {
    height: 100vh;
    width: 100vw;
}

.main-content-page {
    padding: 0;
    background-color: #f7f8fa;
}

.dark .main-content-page {
    background-color: #212225;
}

.main-content-header {
    border-bottom: solid 1px var(--el-border-color);
    height: 55px;
    display: flex;
    align-items: center;
    box-sizing: border-box;

    .logo {
        width: 153px;
        height: 32px;
        transition: opacity .4s;

        &:hover {
            cursor: pointer;
            opacity: .7;
        }
    }

    .user-info {
        display: flex;
        justify-content: flex-end;
        align-items: center;

        .el-avatar:hover {
            cursor: pointer;
        }

        .profile {
            text-align: right;
            margin-right: 20px;

            :first-child {
                font-size: 18px;
                font-weight: bold;
                line-height: 20px;
            }

            :last-child {
                font-size: 10px;
                color: grey;
            }
        }
    }
}
</style>
