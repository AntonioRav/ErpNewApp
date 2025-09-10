// total.js
let salaryChart = null;

const excludedFields = [
    'absent_days', 'branch', 'company', 'data_of_joining', 'department', 'designation',
    'employee', 'employee_name', 'end_date', 'leave_without_pay', 'payment_days',
    'salary_slip_id', 'start_date'
];

const monthNames = [
    '', 'Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin',
    'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre', 'Décembre'
];

let totalsPerMonth = {};

function processSalaryData(salaryData) {
    totalsPerMonth = {}; // reset before filtering again

    salaryData.forEach(entry => {
        const date = entry.start_date || entry.startDate || entry.start || entry.date;
        const month = new Date(date).getMonth() + 1;
        const year = new Date(date).getFullYear();

        const key = `${year}-${month.toString().padStart(2, '0')}`;
        if (!totalsPerMonth[key]) {
            totalsPerMonth[key] = { moisNum: month, anneeNum: year };
        }

        Object.keys(entry).forEach(field => {
            if (!excludedFields.includes(field) && typeof entry[field] === 'number') {
                totalsPerMonth[key][field] = (totalsPerMonth[key][field] || 0) + entry[field];
            }
        });
    });

    updateTable();
    drawLineChart(totalsPerMonth);
}

function updateTable() {
    const headerRow = document.getElementById("salary-table-header");
    const body = document.getElementById("salary-table-body");
    headerRow.innerHTML = '<th>Mois</th>';
    body.innerHTML = '';

    const monthKeys = Object.keys(totalsPerMonth).filter(m => !m.includes('undefined')).sort();
    const allFields = Object.keys(totalsPerMonth[monthKeys[0]] || {}).filter(f => f !== 'moisNum' && f !== 'anneeNum');

    allFields.forEach(field => {
        headerRow.innerHTML += `<th>${field}</th>`;
    });

    monthKeys.forEach(key => {
        const row = document.createElement("tr");
        const moisNum = totalsPerMonth[key].moisNum;
        const anneeNum = totalsPerMonth[key].anneeNum;
        const link = `<a href="/salaryRegisterFiltre?mois=${moisNum}&annee=${anneeNum}">${monthNames[moisNum]} ${anneeNum}</a>`;
        row.innerHTML = `<td>${link}</td>`;
        allFields.forEach(field => {
            row.innerHTML += `<td>${totalsPerMonth[key][field]?.toFixed(2) || 0}</td>`;
        });
        body.appendChild(row);
    });
}

function drawLineChart(totalsPerMonth) {
    const selectedYear = parseInt(document.getElementById('yearFilter').value) || 2025;
    const currentYear = selectedYear;
    const fullMonthKeys = Array.from({ length: 12 }, (_, i) => {
        const month = (i + 1).toString().padStart(2, '0');
        return `${currentYear}-${month}`;
    });

    // Récupérer tous les champs sauf les exclus (moisNum, anneeNum)
    const firstExistingMonth = Object.keys(totalsPerMonth)[0];
    const allFields = Object.keys(totalsPerMonth[firstExistingMonth] || {}).filter(
        f => f !== 'moisNum' && f !== 'anneeNum'
    );

    const datasets = allFields.map(field => {
        return {
            label: field,
            data: fullMonthKeys.map(month => (totalsPerMonth[month]?.[field]) || 0),
            borderColor: getRandomColor(),
            tension: 0.3,
            fill: false
        };
    });

    const ctx = document.getElementById('salaryLineChart').getContext('2d');

    // 🔥 DÉTRUIRE L'ANCIEN GRAPHIQUE AVANT D'EN CRÉER UN NOUVEAU
    if (salaryChart) {
        salaryChart.destroy();
    }

    salaryChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: fullMonthKeys.map(m => {
                const [year, month] = m.split("-");
                return `${monthNames[parseInt(month)]} ${year}`;
            }),
            datasets: datasets
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Évolution mensuelle des totaux'
                },
                tooltip: {
                    mode: 'index',
                    intersect: false
                },
                legend: {
                    position: 'bottom'
                }
            },
            interaction: {
                mode: 'nearest',
                axis: 'x',
                intersect: false
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}


function getRandomColor() {
    return `hsl(${Math.floor(Math.random() * 360)}, 70%, 50%)`;
}

function populateYearFilter() {
    const yearFilter = document.getElementById("yearFilter");
    const years = new Set();
    salaryData.forEach(entry => {
        const year = new Date(entry.start_date || entry.date).getFullYear();
        years.add(year);
    });

    [...years].sort().forEach(y => {
        const option = document.createElement("option");
        option.value = y;
        option.textContent = y;
        yearFilter.appendChild(option);
    });
}

function applyFilters() {
    const selectedMonth = document.getElementById("monthFilter").value;
    const selectedYear = document.getElementById("yearFilter").value;

    const filteredData = salaryData.filter(entry => {
        const date = new Date(entry.start_date || entry.date);
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const year = date.getFullYear().toString();
        return (!selectedMonth || month === selectedMonth) && (!selectedYear || year === selectedYear);
    });

    processSalaryData(filteredData);
}

// Au chargement initial
window.addEventListener('DOMContentLoaded', () => {
    populateYearFilter();
    processSalaryData(salaryData);

    document.getElementById("applyFilterBtn").addEventListener("click", applyFilters);
});
