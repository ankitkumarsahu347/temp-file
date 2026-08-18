const form = document.getElementById('todoForm');
const input = document.getElementById('todoInput');
const list = document.getElementById('todoList');
const count = document.getElementById('taskCount');
const emptyState = document.getElementById('emptyState');
const clearCompleted = document.getElementById('clearCompleted');

let todos = JSON.parse(localStorage.getItem('todos') || '[]');

function save() {
  localStorage.setItem('todos', JSON.stringify(todos));
}

function render() {
  list.innerHTML = '';

  todos.forEach((todo, index) => {
    const item = document.createElement('li');
    item.className = `todo-item ${todo.completed ? 'completed' : ''}`;

    const checkbox = document.createElement('input');
    checkbox.type = 'checkbox';
    checkbox.checked = todo.completed;
    checkbox.addEventListener('change', () => {
      todos[index].completed = checkbox.checked;
      save();
      render();
    });

    const text = document.createElement('span');
    text.textContent = todo.text;

    const deleteButton = document.createElement('button');
    deleteButton.className = 'delete';
    deleteButton.textContent = 'Delete';
    deleteButton.addEventListener('click', () => {
      todos.splice(index, 1);
      save();
      render();
    });

    item.append(checkbox, text, deleteButton);
    list.appendChild(item);
  });

  const remaining = todos.filter(todo => !todo.completed).length;
  count.textContent = `${remaining} ${remaining === 1 ? 'task' : 'tasks'} remaining`;
  emptyState.style.display = todos.length ? 'none' : 'block';
}

form.addEventListener('submit', event => {
  event.preventDefault();
  const text = input.value.trim();
  if (!text) return;

  todos.push({ text, completed: false });
  input.value = '';
  save();
  render();
  input.focus();
});

clearCompleted.addEventListener('click', () => {
  todos = todos.filter(todo => !todo.completed);
  save();
  render();
});

render();
