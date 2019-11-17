cd ant-design-vue-jeecg
npm run build

cd ..
rd /s /q jeecg-boot\jeecg-boot-module-system\src\main\resources\static\css
rd /s /q jeecg-boot\jeecg-boot-module-system\src\main\resources\static\img
rd /s /q jeecg-boot\jeecg-boot-module-system\src\main\resources\static\js
xcopy /YSF ant-design-vue-jeecg\dist\*.* jeecg-boot\jeecg-boot-module-system\src\main\resources\static

pause
