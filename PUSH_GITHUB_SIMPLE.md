# 🚀 Guide Simple - Push sur GitHub

## ✅ Vous êtes ICI

Votre Git est configuré et initialisé !

## 📝 3 Commandes Seulement

Copiez et exécutez ces commandes **une par une** dans votre terminal :

```bash
# 1️⃣ Ajouter tous les fichiers
git add .

# 2️⃣ Créer le commit
git commit -m "feat: Module Gestion des Stocks - Initial commit"

# 3️⃣ Ajouter le dépôt GitHub et pousser
git remote add origin https://github.com/GOLITI/supermarket-manager.git
git branch -M main
git push -u origin main
```

## 🔐 Authentification GitHub

Quand vous faites `git push`, GitHub vous demandera :

### Option 1 : Personal Access Token (Recommandé)
1. Allez sur : https://github.com/settings/tokens
2. Cliquez sur **"Generate new token (classic)"**
3. Cochez **"repo"**
4. Copiez le token (commence par `ghp_...`)
5. Utilisez-le comme mot de passe

### Option 2 : GitHub CLI (Plus Simple)
```bash
# Installer GitHub CLI
sudo apt install gh

# Se connecter
gh auth login

# Pousser ensuite
git push -u origin main
```

## ✅ Résultat Attendu

Après `git push`, vous verrez :
```
Enumerating objects: XX, done.
Counting objects: 100%, done.
...
To https://github.com/GOLITI/supermarket-manager.git
 * [new branch]      main -> main
```

👉 Votre projet sera visible sur : **https://github.com/GOLITI/supermarket-manager**

## 📦 Ce qui sera publié

- ✅ 37 classes Java (backend)
- ✅ 43 tests JUnit
- ✅ 6 fichiers de documentation
- ✅ Configuration Spring Boot
- ✅ Scripts SQL
- ✅ README professionnel

---

**C'est tout ! 3 commandes et c'est fait ! 🎉**

