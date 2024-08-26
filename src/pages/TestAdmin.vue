<template>
    <div>
        <div v-if="user">
            <span>{{ user.sub }}</span>
        </div>
        <p v-if="role">User role: {{ role }}</p>
        <p v-else>No role found</p>


        <div v-if="isAdmin">
            <p>This is a special content for admins.</p>
        </div>
    </div>
</template>

<script setup>
import { onMounted, computed } from 'vue';
import { useUserStore } from '@/store/userStore';

const userStore = useUserStore();

const user = computed(() => userStore.user.value);
const role = computed(() => userStore.role.value);

const isAdmin = computed(() => role.value === 'ROLE_ADMIN');

onMounted(async () => {
    await userStore.fetchUser();
});
</script>