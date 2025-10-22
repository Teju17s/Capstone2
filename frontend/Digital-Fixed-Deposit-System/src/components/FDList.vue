<template>
  <div class="fd-list">
    <h2 class="heading">Your Fixed Deposits</h2>

    <div v-if="loading" class="loading">Loading FDs...</div>
    <div v-else-if="fds.length === 0" class="empty">No FDs found.</div>

    <div
      v-for="fd in fds"
      :key="fd.fixedDepositId"
      :id="'fd-' + fd.fixedDepositId"
      @click="toggle(fd.fixedDepositId)"
      :class="['fd-card', { expanded: isExpanded(fd.fixedDepositId) }]"
    >
      <div class="fd-summary">
        <div class="fd-info">
          <div class="scheme-name">{{ fd.scheme || '—' }}</div>
          <div class="amount">₹{{ formatCurrency(fd.amount) }}</div>
          <div class="tenure">{{ fd.tenureMonths }} months • {{ fd.interestRate }}% p.a.</div>
        </div>

        <div class="fd-status">
          <div class="status" :style="statusStyle(fd.status)">{{ fd.status }}</div>
          <div class="maturity-label">Matures on</div>
          <div class="maturity-date">{{ formatDate(fd.maturityDate) }}</div>
        </div>
      </div>

      <transition name="fade">
        <div v-if="isExpanded(fd.fixedDepositId)" class="fd-details">
          <div class="detail-grid">
            <div><strong>FD ID:</strong> {{ fd.fixedDepositId }}</div>
            <div><strong>User ID:</strong> {{ fd.userId }}</div>
            <div><strong>Scheme:</strong> {{ fd.scheme }}</div>
            <div><strong>Amount:</strong> ₹{{ formatCurrency(fd.amount) }}</div>
            <div><strong>Interest Rate:</strong> {{ fd.interestRate }}% p.a.</div>
            <div><strong>Tenure:</strong> {{ fd.tenureMonths }} months</div>
            <div><strong>Start Date:</strong> {{ formatDate(fd.startDate) }}</div>
            <div><strong>Maturity Date:</strong> {{ formatDate(fd.maturityDate) }}</div>
            <div><strong>Accrued Interest:</strong> ₹{{ formatCurrency(accruedInterest(fd)) }}</div>
            <div><strong>Maturity Value:</strong> ₹{{ formatCurrency(maturityValue(fd)) }}</div>
            <div><strong>Status:</strong> {{ fd.status }}</div>
          </div>
        </div>
      </transition>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { getFds } from '@/config/api.js';
import logger from '@/utils/logger.js';

export default {
  name: 'FDList',
  setup() {
    const route = useRoute();
    const fds = ref([]);
    const loading = ref(true);
    const expandedId = ref(null);

    const loadFds = async () => {
      loading.value = true;
      try {
        const res = await getFds(1);
        fds.value = Array.isArray(res.data) ? res.data : Array.isArray(res) ? res : [];
        logger.info('Fetched FDs', { count: fds.value.length });
      } catch (err) {
        logger.error('Failed to fetch FDs', err);
        fds.value = [];
      } finally {
        loading.value = false;
      }
    };

    const toggle = (id) => {
      expandedId.value = expandedId.value === id ? null : id;
    };

    const isExpanded = (id) => expandedId.value === id;

    const formatCurrency = (value) =>
      Number(value || 0).toLocaleString('en-IN', { maximumFractionDigits: 0 });

    const formatDate = (d) => (d ? new Date(d).toLocaleDateString('en-GB') : '—');

    const accruedInterest = (fd) => {
      const principal = Number(fd.amount || 0);
      const rate = Number(fd.interestRate || 0) / 100;
      const start = new Date(fd.startDate);
      const today = new Date();
      const end = new Date(fd.maturityDate);
      const effectiveDate = today < end ? today : end;
      const days = Math.max(0, Math.floor((effectiveDate - start) / (1000 * 60 * 60 * 24)));
      return Math.round(principal * rate * (days / 365));
    };

    const maturityValue = (fd) => Math.round(Number(fd.amount || 0) + accruedInterest(fd));

    const statusStyle = (status) => {
      const base = { padding: '6px 10px', borderRadius: '14px', fontSize: '12px', fontWeight: 700 };
      if (status === 'ACTIVE') return { ...base, background: '#e8f5e9', color: '#2e7d32' };
      if (status === 'MATURED') return { ...base, background: '#fff3e0', color: '#ef6c00' };
      if (status === 'BROKEN') return { ...base, background: '#ffebee', color: '#c62828' };
      return { ...base, background: '#f3f4f6', color: '#374151' };
    };

    onMounted(loadFds);

    watch(
      () => route.query.newFdId,
      (id) => {
        if (id) {
          loadFds().then(() => {
            expandedId.value = Number(id);
            setTimeout(() => {
              const el = document.getElementById('fd-' + id);
              if (el) el.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }, 100);
          });
        }
      },
      { immediate: true }
    );

    return {
      fds,
      loading,
      expandedId,
      toggle,
      isExpanded,
      formatCurrency,
      formatDate,
      accruedInterest,
      maturityValue,
      statusStyle
    };
  }
};
</script>

<style scoped>
.fd-list {
  max-width: 1100px;
  margin: 0 auto;
  padding: 32px 20px;
}

.heading {
  text-align: center;
  font-size: 26px;
  font-weight: 700;
  margin-bottom: 24px;
  color: #1a237e;
}

.loading, .empty {
  text-align: center;
  padding: 18px;
  font-size: 15px;
  color: #555;
  background: #fafafa;
  border-radius: 10px;
}

.fd-card {
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.07);
  padding: 20px 24px;
  margin-bottom: 18px;
  border: 1px solid #eee;
  transition: all 0.2s ease;
  cursor: pointer;
}

.fd-card.expanded {
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
  border-color: #d0e3ff;
}

.fd-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
}

.fd-info {
  flex: 1;
  min-width: 240px;
}

.scheme-name {
  font-size: 15px;
  font-weight: 600;
  color: #374151;
  margin-bottom: 4px;
}

.amount {
  font-size: 22px;
  font-weight: 700;
  color: #1e40af;
}

.tenure {
  font-size: 13px;
  color: #555;
  margin-top: 4px;
}

.fd-status {
  text-align: right;
  min-width: 180px;
}

.status {
  display: inline-block;
  margin-bottom: 8px;
}

.maturity-label {
  font-size: 12px;
  color: #777;
}

.maturity-date {
  font-weight: 600;
  color: #374151;
}

.fd-details {
  margin-top: 14px;
  background: #f8fafc;
  border-radius: 10px;
  border: 1px solid #e5e7eb;
  padding: 16px;
}

.detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px 18px;
  font-size: 13px;
  color: #444;
}

.fade-enter-active, .fade-leave-active {
  transition: all 0.2s ease;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
  transform: translateY(-6px);
}
</style>
