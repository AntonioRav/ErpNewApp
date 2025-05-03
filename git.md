-creer gitignore
# Initialiser un nouveau repository Git
git init

# Ajouter tous les fichiers au staging
git add .

# Créer le premier commit
git commit -m "Initial commit: Application de gestion des devis et commandes"

# Si vous voulez lier à un repository distant (par exemple sur GitHub)
# Remplacez [URL_REPOSITORY] par l'URL de votre repository GitHub
git remote add origin https://github.com/AntonioRav/ErpNewApp.git
git branch -M main
git push -f origin main(attention, cela supprimera tout ce qui est sur GitHub)