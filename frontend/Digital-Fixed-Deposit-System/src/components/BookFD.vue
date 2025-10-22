<template>
  <div class="fd-card">
    <h3>Book FD</h3>

    <div class="scheme-selection">
      <div 
        v-for="scheme in schemes" 
        :key="scheme.name" 
        :class="['scheme', { selected: selected?.name === scheme.name }]" 
        @click="selectScheme(scheme)"
      >
        {{ scheme.name }}
      </div>
    </div>

    <div v-if="selected" class="fd-details">
      <label>Principal Amount:</label>
      <input type="number" v-model.number="principal" min="1000" />

      <label>Tenure (months):</label>
      <select v-model.number="tenure">
        <option v-for="t in tenures" :key="t" :value="t">{{ t }}</option>
      </select>

      <p>Total Interest: {{ formatCurrency(totalInterest) }}</p>
      <p>Maturity Value: {{ formatCurrency(maturityValue) }}</p>

      <button :disabled="loading" @click="submit">
        {{ loading ? 'Booking...' : 'Book FD' }}
      </button>
    </div>
  </div>
</template>

<script>
import { SCHEMES } from '@/constants/schemes.js';
import { bookFd } from '@/config/api.js';
import logger from '@/utils/logger.js';

export default {
  name: 'BookFD',
  data() {
    return {
      schemes: SCHEMES,
      selected: null,
      principal: null,
      tenure: null,
      loading: false
    };
  },
  computed: {
    tenures() {
      return this.selected 
        ? Array.from({ length: this.selected.max - this.selected.min + 1 }, (_, i) => i + this.selected.min) 
        : [];
    },
    totalInterest() {
      if (!this.principal || !this.tenure || !this.selected) return 0;
      return (this.principal * this.selected.rate * this.tenure) / 1200;
    },
    maturityValue() {
      return (this.principal || 0) + this.totalInterest;
    }
  },
  methods: {
    selectScheme(scheme) {
      this.selected = scheme;
      this.tenure = scheme.min;
    },
    formatCurrency(value) {
      return new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(value || 0);
    },
    async submit() {
      if (!this.principal || !this.tenure || !this.selected) {
        alert('Please fill all details!');
        return;
      }

      const payload = {
        userId: 1, // replace dynamically if available
        amount: this.principal,
        tenureMonths: this.tenure,
        scheme: this.selected.name,
        interestRate: this.selected.rate,
        startDate: new Date().toISOString()
      };

      this.loading = true;
      try {
        const res = await bookFd(payload);
        logger.info('FD booked successfully', payload);

        // Reset form
        this.principal = null;
        this.tenure = this.selected.min;

        // Navigate to FD list and expand newly booked FD
        this.$router.push({ path: '/fd-list', query: { newFdId: res.id || Date.now() } });
      } catch (err) {
        logger.error('Failed to book FD', err, payload);
        alert('Error booking FD. Check console.');
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
.fd-card { width: 420px; padding: 16px; background: #fff; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.08); margin: 0 auto; }
.scheme-selection { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 12px; }
.scheme { padding: 8px; background: #f0f0f0; border-radius: 6px; cursor: pointer; text-align: center; }
.scheme.selected { background: #1890ff; color: #fff; }
.fd-details label { display: block; margin: 6px 0 2px; }
.fd-details input, .fd-details select { width: 100%; padding: 6px; margin-bottom: 6px; border-radius: 6px; border: 1px solid #ddd; }
.fd-details button { width: 100%; background: #1890ff; color: white; padding: 8px 12px; border: none; border-radius: 6px; cursor: pointer; font-weight: 600; }
</style>
