call rd /s /q ant-design-vue-jeecg\dist

call rd /s /q jeecg-boot\jeecg-boot-module-system\src\main\resources\static\css

call rd /s /q jeecg-boot\jeecg-boot-module-system\src\main\resources\static\img

call rd /s /q jeecg-boot\jeecg-boot-module-system\src\main\resources\static\js

cd ant-design-vue-jeecg

call npm run build

cd ..

call xcopy /YSF ant-design-vue-jeecg\dist\*.* jeecg-boot\jeecg-boot-module-system\src\main\resources\static

pause
