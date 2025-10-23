<template>
  <div class="fd-card">
    <h3 class="fd-title">Book FD</h3>

    <!-- Scheme Selection -->
    <div class="scheme-selection">
      <div
        v-for="scheme in schemes"
        :key="scheme.name"
        :class="['scheme-card', { selected: selected?.name === scheme.name }]"
        @click="selectScheme(scheme)"
      >
        <div class="scheme-header">{{ scheme.name }}</div>
        <div class="scheme-rate">{{ scheme.rate }}% p.a.</div>
      </div>
    </div>

    <!-- FD Input & Details -->
    <div v-if="selected" class="fd-details">
      <label for="principal">Principal Amount:</label>
      <input
        id="principal"
        type="number"
        v-model.number="principal"
        min="1000"
        placeholder="Enter amount (min ₹1,000)"
      />

      <label for="tenure">Tenure (months):</label>
      <select id="tenure" v-model.number="tenure">
        <option v-for="t in tenures" :key="t" :value="t">{{ t }}</option>
      </select>

      <div class="fd-summary">
        <p><strong>Total Interest:</strong> {{ formatCurrency(totalInterest) }}</p>
        <p><strong>Maturity Value:</strong> {{ formatCurrency(maturityValue) }}</p>
      </div>

      <button
        class="book-btn"
        :disabled="loading"
        @click="submit"
      >
        {{ loading ? 'Booking...' : 'Book FD' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { SCHEMES } from '@/constants/schemes.js'
import { bookFd } from '@/config/api.js'
import logger from '@/utils/logger.js'

// --- Router ---
const router = useRouter()

// --- Reactive State ---
const schemes = ref(SCHEMES)
const selected = ref(null)
const principal = ref(null)
const tenure = ref(null)
const loading = ref(false)

// --- Computed Properties ---
const tenures = computed(() => {
  if (!selected.value) return []
  const { min, max } = selected.value
  return Array.from({ length: max - min + 1 }, (_, i) => min + i)
})

const totalInterest = computed(() => {
  if (!principal.value || !tenure.value || !selected.value) return 0
  return (principal.value * selected.value.rate * tenure.value) / 1200
})

const maturityValue = computed(() => {
  return (principal.value || 0) + totalInterest.value
})

// --- Methods ---
const selectScheme = (scheme) => {
  selected.value = scheme
  tenure.value = scheme.min
}

const formatCurrency = (value) => {
  return new Intl.NumberFormat('en-IN', {
    style: 'currency',
    currency: 'INR'
  }).format(value || 0)
}

const submit = async () => {
  if (!principal.value || !tenure.value || !selected.value) {
    alert('Please fill all details!')
    return
  }

  const payload = {
    userId: 1, // TODO: Replace dynamically when login integration is done
    amount: principal.value,
    tenureMonths: tenure.value,
    scheme: selected.value.name,
    interestRate: selected.value.rate,
    startDate: new Date().toISOString()
  }

  loading.value = true
  try {
    const res = await bookFd(payload)
    logger.info('FD booked successfully', payload)

    // Reset fields
    principal.value = null
    tenure.value = selected.value.min

    router.push({
      path: '/fd-list',
      query: { newFdId: res.id || Date.now() }
    })
  } catch (error) {
    logger.error('Failed to book FD', error, payload)
    alert('Error booking FD. Check console for details.')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* --- Card Container --- */
.fd-card {
  width: 420px;
  padding: 20px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  margin: 0 auto;
}

.fd-title {
  text-align: center;
  margin-bottom: 16px;
  font-weight: 600;
  color: #222;
}

/* --- Scheme Selection --- */
.scheme-selection {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  margin-bottom: 16px;
}

.scheme-card {
  padding: 12px;
  background: #f8f9fc;
  border-radius: 8px;
  cursor: pointer;
  text-align: center;
  border: 1px solid #e0e0e0;
  transition: all 0.25s ease;
}

.scheme-card:hover {
  background: #eef5ff;
  transform: translateY(-2px);
}

.scheme-card.selected {
  background: #1890ff;
  color: #ffffff;
  border-color: #1890ff;
  transform: scale(1.03);
}

.scheme-header {
  font-weight: 600;
  font-size: 15px;
  margin-bottom: 4px;
}

.scheme-rate {
  font-size: 14px;
  font-weight: 500;
  color: #444;
}

.scheme-card.selected .scheme-rate {
  color: #eaf5ff;
}

/* --- FD Details --- */
.fd-details label {
  display: block;
  margin: 6px 0 2px;
  font-weight: 500;
}

.fd-details input,
.fd-details select {
  width: 100%;
  padding: 8px;
  margin-bottom: 8px;
  border-radius: 6px;
  border: 1px solid #ddd;
  font-size: 14px;
}

.fd-summary {
  background: #f8faff;
  padding: 10px;
  border-radius: 6px;
  margin-top: 8px;
}

.fd-summary p {
  font-size: 14px;
  margin: 4px 0;
}

/* --- Button --- */
.book-btn {
  width: 100%;
  background: #1890ff;
  color: #ffffff;
  padding: 10px 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  margin-top: 10px;
  transition: background 0.25s ease, transform 0.1s ease;
}

.book-btn:hover {
  background: #1765c0;
  transform: scale(1.02);
}

.book-btn:disabled {
  background: #b0c4de;
  cursor: not-allowed;
}
</style>
