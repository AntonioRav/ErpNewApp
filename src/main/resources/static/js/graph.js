// Champs à ne pas dessiner dans le graphique
const excludedFields = ['absent_days', 'branch', 'company', 'data_of_joining', 'department','designation','employee','employee_name','end_date','leave_without_pay','payment_days','salary_slip_id','start_date',];

// Déterminer dynamiquement les labels (dates/mois)
const labels = salaryData.map(entry => entry.start_date || entry.month || '');

// Récupérer tous les champs possibles à partir du premier entry
const allFields = Object.keys(salaryData[0]);

// Garder uniquement les champs qu’on veut dessiner
const includedFields = allFields.filter(key => !excludedFields.includes(key));

// Construction dynamique des datasets pour Chart.js
const datasets = includedFields.map(field => ({
    label: field,
    data: salaryData.map(entry => entry[field]),
    backgroundColor: randomColor(), // Une couleur aléatoire pour chaque ligne
    borderColor: randomColor(),
    borderWidth: 2,
    fill: false
}));

// Fonction pour générer une couleur aléatoire
function randomColor() {
    const r = Math.floor(Math.random() * 255);
    const g = Math.floor(Math.random() * 255);
    const b = Math.floor(Math.random() * 255);
    return `rgba(${r}, ${g}, ${b}, 0.6)`;
}

// Création du graphique
const ctx = document.getElementById('salaryChart').getContext('2d');
new Chart(ctx, {
    type: 'line',
    data: {
        labels: labels,
        datasets: datasets
    },
    options: {
        responsive: true,
        plugins: {
            title: {
                display: true,
                text: 'Évolution des composantes de salaire'
            }
        }
    }
});
