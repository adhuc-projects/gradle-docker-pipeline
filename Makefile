ifeq ($(OS),Windows_NT)
	gradle = gradlew.cmd
else
	gradle = ./gradlew
endif

build: clean ## Build the application
	$(gradle) test
	$(gradle) bootJar docker

test: ## Run acceptance testing
	$(gradle) acceptance

run: ## Run the application
	$(gradle) bootRun

up: ## Start the application and its dependencies
	docker-compose -f docker/docker-compose.yml up -d

down: ## Stop the application and its dependencies
	docker-compose -f docker/docker-compose.yml down

clean: ## Clean the project folder
	$(gradle) clean dockerClean

help: ## This help dialog.
	@echo "Usage: make [target]. Find the available targets below:"
	@echo "$$(grep -hE '^\S+:.*##' $(MAKEFILE_LIST) | sed 's/:.*##\s*/:/' | column -c2 -t -s :)"
