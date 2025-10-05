# 🚀 Guide de Push sur GitHub

## ✅ Configuration Effectuée

Git a été configuré avec :
- **Email** : marcgoliti429@gmail.com
- **Nom** : GOLITI
- **Dépôt** : git@github.com:GOLITI/supermarket-manager.git

## 📝 Commandes à Exécuter

Ouvrez un terminal et exécutez les commandes suivantes **une par une** :

```bash
# 1. Aller dans le répertoire du projet
cd /home/goliti/PROJETS/AGLPROJET/supermarket-manager

# 2. Vérifier l'état de Git
git status

# 3. Ajouter tous les fichiers
git add .

# 4. Créer le commit
git commit -m "feat: Module Gestion des Stocks complet - Initial commit"

# 5. Vérifier que le commit a été créé
git log --oneline -1

# 6. Pousser sur GitHub
git push -u origin main
```

## 🔑 Configuration SSH GitHub (si nécessaire)

Si le push échoue avec une erreur SSH, configurez votre clé SSH :

```bash
# 1. Générer une clé SSH (si vous n'en avez pas)
ssh-keygen -t ed25519 -C "marcgoliti429@gmail.com"

# 2. Afficher votre clé publique
cat ~/.ssh/id_ed25519.pub

# 3. Copier la clé et l'ajouter sur GitHub :
#    - Aller sur : https://github.com/settings/keys
#    - Cliquer sur "New SSH key"
#    - Coller la clé et sauvegarder

# 4. Tester la connexion
ssh -T git@github.com

# 5. Réessayer le push
git push -u origin main
```

## 🌐 Alternative : Utiliser HTTPS

Si SSH ne fonctionne pas, utilisez HTTPS :

```bash
# 1. Changer l'URL du remote
git remote set-url origin https://github.com/GOLITI/supermarket-manager.git

# 2. Pousser avec HTTPS
git push -u origin main

# GitHub vous demandera votre nom d'utilisateur et un Personal Access Token
# Pour créer un token : https://github.com/settings/tokens
```

## ✅ Vérification

Après le push, vérifiez que votre code est bien sur GitHub :
👉 **https://github.com/GOLITI/supermarket-manager**

## 📊 Contenu Poussé

Votre repository contiendra :
- ✅ 37 classes Java (production)
- ✅ 5 classes de tests (43 tests)
- ✅ Documentation complète (6 fichiers MD)
- ✅ Configuration Spring Boot
- ✅ Scripts SQL
- ✅ .gitignore configuré

## 🆘 Dépannage

### Erreur : "fatal: impossible de détecter automatiquement l'adresse"
**Solution** : ✅ Déjà corrigé - Git configuré avec votre email

### Erreur : "le spécificateur de référence source main ne correspond à aucune référence"
**Cause** : Aucun commit créé
**Solution** : Exécuter les commandes 3 et 4 ci-dessus

### Erreur : "Permission denied (publickey)"
**Cause** : Clé SSH non configurée
**Solution** : Suivre la section "Configuration SSH GitHub"

## 📞 Support

Si vous rencontrez des problèmes :
1. Vérifier les messages d'erreur exacts
2. Consulter : https://docs.github.com/fr/get-started
3. Ou utiliser HTTPS au lieu de SSH

---

**Une fois le push réussi, votre projet sera visible publiquement sur GitHub ! 🎉**

